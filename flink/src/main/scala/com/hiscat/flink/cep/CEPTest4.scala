package com.hiscat.flink.cep

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object CEPTest4 {

  case class OrderEvent(id: String, eventType: String, ts: Long)


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val input = env.fromElements(
      OrderEvent("1", "create", 1000),
      OrderEvent("2", "create", 1000),
      OrderEvent("2", "pay", 2000)
    )
      .assignAscendingTimestamps(_.ts)
      .keyBy(_.id)
      .process(new KeyedProcessFunction[String, OrderEvent, String] {
        lazy val pay: ValueState[OrderEvent] =
          getRuntimeContext.getState(new ValueStateDescriptor[OrderEvent]("pay", classOf[OrderEvent]))

        override def processElement(value: OrderEvent, ctx: KeyedProcessFunction[String, OrderEvent, String]#Context, out: Collector[String]): Unit = {
          value.eventType match {
            case "pay" => pay.update(value)
            case "create" => if (pay.value() == null) pay.update(value)
          }
          ctx.timerService().registerEventTimeTimer(value.ts + 5000L)
        }

        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, OrderEvent, String]#OnTimerContext, out: Collector[String]): Unit = {
          pay.value().eventType match {
            case "create" => out.collect(s"not pay order: ${ctx.getCurrentKey}")
            case _ =>
          }
        }
      })
      .print()

    env.execute()
  }
}
