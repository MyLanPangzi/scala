package com.hiscat.flink.time

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ProcessTimeTimer {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.socketTextStream("localhost", 9001)
      .map(_.split(" "))
      .map(e => (e(0), e(1).toLong * 1000))
      .keyBy(_._1)
      .process(new KeyedProcessFunc)
      .print()

    env.execute()
  }

  class KeyedProcessFunc extends KeyedProcessFunction[String, (String, Long), String] {
    override def processElement(value: (String, Long),
                                ctx: KeyedProcessFunction[String, (String, Long), String]#Context,
                                out: Collector[String]): Unit = {
      ctx.timerService().registerProcessingTimeTimer(System.currentTimeMillis() + 3000)
      out.collect(value._1)
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[String, (String, Long), String]#OnTimerContext, out: Collector[String]): Unit = {
      println(s"$timestamp fired")
    }
  }

}
