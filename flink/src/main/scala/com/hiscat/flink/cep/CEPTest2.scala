package com.hiscat.flink.cep

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

import scala.collection.JavaConversions._

object CEPTest2 {

  case class Event(username: String, eventType: String, ip: String, ts: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)


    env.fromElements(
      Event("1", "fail", "192.1.1.2", 1),
      Event("1", "fail", "192.1.1.3", 2),
      Event("1", "fail", "192.1.1.4", 3)
//      Event("1", "success", "192.1.1.5", 4)
    )
      .assignAscendingTimestamps(_.ts * 1000)
      .keyBy(_.username)
      .process(new KeyedProcessFunction[String, Event, String] {
        lazy val logins: ListState[Event] =
          getRuntimeContext.getListState(new ListStateDescriptor[Event]("logins", classOf[Event]))
        lazy val timer: ValueState[Long] =
          getRuntimeContext.getState(new ValueStateDescriptor[Long]("timer", classOf[Long]))

        override def processElement(value: Event, ctx: KeyedProcessFunction[String, Event, String]#Context, out: Collector[String]): Unit = {
          value.eventType match {
            case "fail" =>
              logins.add(value)
              if (logins.get().size >= 3) {
                ctx.timerService().deleteEventTimeTimer(timer.value())
                timer.update(value.ts * 1000 + 10000L)
                ctx.timerService().registerEventTimeTimer(timer.value())
              }
            case "success" =>
              logins.clear()
              ctx.timerService().deleteEventTimeTimer(timer.value())
              timer.clear()
            case _ =>
          }
        }

        override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, Event, String]#OnTimerContext, out: Collector[String]): Unit = {
          out.collect(
            s"""
               |k:${ctx.getCurrentKey}, fail count:${logins.get().size}
               |""".stripMargin)
        }
      })
      .print()


    env.execute()
  }
}
