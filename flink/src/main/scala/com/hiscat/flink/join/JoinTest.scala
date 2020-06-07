package com.hiscat.flink.join

import org.apache.flink.api.common.functions.JoinFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object JoinTest {

  case class Order(id: String, eventType: String, ts: Long)

  case class Pay(orderId: String, eventType: String, ts: Long)


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val orders = env.fromElements(
      Order("1", "create", 1000),
      Order("2", "create", 5000),
      Order("3", "create", 10000)
    )
      .assignAscendingTimestamps(_.ts)

    val pay = env.fromElements(
      Pay("1", "pay", -1000),
      Pay("2", "pay", 9000),
      Pay("3", "pay", 16000)
    )
      .assignAscendingTimestamps(_.ts)

    orders.join(pay)
      .where(_.id)
      .equalTo(_.orderId)
      .window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .apply(new JoinFunction[Order, Pay, String] {
        override def join(first: Order, second: Pay): String = {
          s"joined order ${first.id}"
        }
      })
      .print()

    env.execute("interval join")
  }


}
