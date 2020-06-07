package com.hiscat.flink.join

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ConnectedTest {

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
      Pay("3", "pay", 4000),
      Pay("4", "pay", 5000),
      Pay("5", "pay", 6000)
    )
      .assignAscendingTimestamps(_.ts)
      .keyBy(_.orderId)

    val connected = orders.connect(pay)
      .process(new KeyedCoProcessFunction[String, Order, Pay, String] {
        lazy val order: ValueState[Order] =
          getRuntimeContext.getState(new ValueStateDescriptor[Order]("order", classOf[Order]))
        lazy val pay: ValueState[Pay] =
          getRuntimeContext.getState(new ValueStateDescriptor[Pay]("pay", classOf[Pay]))
        val delay = 5000L

        override def processElement1(value: Order, ctx: KeyedCoProcessFunction[String, Order, Pay, String]#Context, out: Collector[String]): Unit = {

          if (pay.value() != null) {
            ctx.timerService().deleteEventTimeTimer(pay.value().ts + delay)
            pay.clear()
            out.collect(s"order connected:${value.id}")
          } else {
            order.update(value)
            ctx.timerService().registerEventTimeTimer(value.ts + delay)
          }
        }

        override def processElement2(value: Pay, ctx: KeyedCoProcessFunction[String, Order, Pay, String]#Context, out: Collector[String]): Unit = {
          if (order.value() != null) {
            ctx.timerService().deleteEventTimeTimer(order.value().ts + delay)
            order.clear()
            out.collect(s"order connected:${value.orderId}")
          } else {
            pay.update(value)
            ctx.timerService().registerEventTimeTimer(value.ts + delay)
          }
        }

        override def onTimer(timestamp: Long, ctx: KeyedCoProcessFunction[String, Order, Pay, String]#OnTimerContext, out: Collector[String]): Unit = {
          if (order.value() == null) {
            ctx.output(orderFailed, s"order connected failed connect:${ctx.getCurrentKey}")
          }
          if (pay.value() == null) {
            ctx.output(payFailed, s"pay connected failed connect:${ctx.getCurrentKey}")
          }

          order.clear()
          pay.clear()
        }
      })

    connected.getSideOutput(orderFailed).print()
    connected.getSideOutput(payFailed).print()
    connected.print()


    env.execute("ConnectedTest")
  }

  val payFailed: OutputTag[String] = OutputTag[String]("pay failed connect")
  val orderFailed: OutputTag[String] = OutputTag[String]("order failed connect")

}
