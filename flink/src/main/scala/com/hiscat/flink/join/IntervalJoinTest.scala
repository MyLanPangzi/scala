package com.hiscat.flink.join

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object IntervalJoinTest {

  case class Order(id: String, eventType: String, ts: Long)

  case class Pay(orderId: String, eventType: String, ts: Long)


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val orders = env.fromElements(
      Order("1", "create", 1000),
      Order("2", "create", 2000),
      Order("3", "create", 3000)
    )
      .assignAscendingTimestamps(_.ts)
      .keyBy(_.id)

    val pay = env.fromElements(
      Pay("1", "pay", -5000),
      Pay("2", "pay", 5000),
      Pay("3", "pay", 9000)
    )
      .assignAscendingTimestamps(_.ts)
      .keyBy(_.orderId)

    orders.intervalJoin(pay)
      .between(Time.seconds(-5), Time.seconds(5))
      .process(new ProcessJoinFunction[Order, Pay, String] {
        override def processElement(left: Order, right: Pay, ctx: ProcessJoinFunction[Order, Pay, String]#Context, out: Collector[String]): Unit = {
          out.collect(s"connected order:${left.id}")
        }
      })
      .print()


    env.execute("interval join")
  }


}
