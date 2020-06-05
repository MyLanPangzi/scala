package com.hiscat.flink.ecommerce

import java.time.{Instant, LocalDateTime, ZoneId}

import org.apache.flink.api.common.functions.{AggregateFunction, RichAggregateFunction}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object ProductTopN {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env
      .readTextFile("E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\UserBehavior.csv").name("user behavior source")
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
      .keyBy(_.itemId)
      .timeWindow(Time.hours(1), Time.minutes(5))
      .aggregate(new ProductPvAgg,
        (_, w, it: Iterable[ItemViewCount], out: Collector[ItemViewCount]) => {
          it.head.windowEnd = w.getEnd
          out.collect(it.head)
        }
      )
      .keyBy(_.windowEnd)
      .process(new TopNProcess(3))
      .print()


    env.execute()
  }

  class ProductPvAgg extends AggregateFunction[UserBehavior, ItemViewCount, ItemViewCount] {
    override def createAccumulator(): ItemViewCount = ItemViewCount(Long.MinValue, Long.MinValue, 0)

    override def add(value: UserBehavior, accumulator: ItemViewCount): ItemViewCount = {
      accumulator.count = accumulator.count + 1
      accumulator.itemId = value.itemId
      accumulator
    }

    override def getResult(accumulator: ItemViewCount): ItemViewCount = accumulator

    override def merge(a: ItemViewCount, b: ItemViewCount): ItemViewCount = {
      a.count = a.count + b.count
      a
    }
  }

  class TopNProcess(top: Int) extends KeyedProcessFunction[Long, ItemViewCount, String] {

    lazy val items: ListState[ItemViewCount] =
      getRuntimeContext.getListState(new ListStateDescriptor[ItemViewCount]("items", classOf[ItemViewCount]))

    override def processElement(value: ItemViewCount,
                                ctx: KeyedProcessFunction[Long, ItemViewCount, String]#Context,
                                out: Collector[String]): Unit = {
      items.add(value)
      ctx.timerService().registerEventTimeTimer(value.windowEnd + 100)
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[Long, ItemViewCount, String]#OnTimerContext,
                         out: Collector[String]): Unit = {

      import scala.collection.JavaConverters._
      val result = items
        .get().asScala.toList
        .sortBy(_.count)
        .reverse.take(top)
        .map(e => (e.itemId, e.count))
        .mkString(",")
      val str = Instant.ofEpochMilli(timestamp - 100).atZone(ZoneId.systemDefault()).toLocalDateTime + " : " + result

      items.clear()
      out.collect(str)

    }
  }

}
