package com.hiscat.flink.time

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object EventTimeTimer {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.socketTextStream("localhost", 9000)
      .map(_.split(" "))
      .map(e => (e(0), e(1).toLong * 1000))
      .assignAscendingTimestamps(_._2)
      .keyBy(_._1)
      .process(new KeyedProcessFunc)
      .print()

    env.execute()
  }

  class KeyedProcessFunc extends KeyedProcessFunction[String, (String, Long), String] {
    override def processElement(value: (String, Long),
                                ctx: KeyedProcessFunction[String, (String, Long), String]#Context,
                                out: Collector[String]): Unit = {
      ctx.timerService().registerEventTimeTimer(value._2 + 10000 - 1)
      out.collect(value._1)
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[String, (String, Long), String]#OnTimerContext, out: Collector[String]): Unit = {
      println(s"$timestamp fired")
    }
  }

}
