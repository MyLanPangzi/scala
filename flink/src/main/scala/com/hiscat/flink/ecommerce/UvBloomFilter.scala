package com.hiscat.flink.ecommerce

import com.hiscat.flink.ecommerce.Uv.UvProcess
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{EventTimeTrigger, Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

object UvBloomFilter {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val path = "E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\UserBehavior.csv"
    //    case class UserBehavior(
    //                             userId: Long,
    //                             itemId: Long,
    //                             categoryId: Int,
    //                             behavior: String,
    //                             timestamp: Long)
    //UserBehavior -> (hourEnd,uvCount)
    env
      .readTextFile(path)
      .map(_.split(","))
      .map(strings => {
        UserBehavior(
          userId = strings(0).toLong,
          itemId = strings(1).toLong,
          categoryId = strings(2).toInt,
          behavior = strings(3),
          timestamp = strings(4).toLong
        )
      })
      .filter(_.behavior == "pv")
      .assignAscendingTimestamps(_.timestamp * 1000)
      .timeWindowAll(Time.hours(1))
      .trigger(new Trigger[UserBehavior, TimeWindow] {
        override def onElement(element: UserBehavior, timestamp: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult =
          TriggerResult.FIRE_AND_PURGE

        override def onProcessingTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

        override def onEventTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult =
          if (ctx.getCurrentWatermark > window.getEnd) TriggerResult.FIRE_AND_PURGE else TriggerResult.CONTINUE

        override def clear(window: TimeWindow, ctx: Trigger.TriggerContext): Unit = {}
      })
      .process(new ProcessAllWindowFunction[UserBehavior, String, TimeWindow] {

        lazy val jedis = new Jedis("hadoop102")

        override def process(context: Context, elements: Iterable[UserBehavior], out: Collector[String]): Unit = {
          val key = context.window.getEnd.toString
          val hash = "UserCountHash"
          var count = 0L
          if (jedis.exists(hash) && jedis.hexists(hash, key)) {
            count = jedis.hget(hash, key).toLong + 1
          }
          val offset = bloomHash(elements.head.userId.toString, 1 << 29)
          if (!jedis.getbit(key, offset)) {
            jedis.setbit(key, offset, true)
            jedis.hset(hash, key, (count + 1).toString)
          }

        }

        def bloomHash(userId: String, bitArraySize: Long): Long = {
          var result = 0
          for (i <- 0 until userId.length) {
            result = result * 61 + userId.charAt(i)
          }
          (bitArraySize - 1) & result
        }

        override def close(): Unit = jedis.close()
      })
      .print()


    env.execute()
  }


}
