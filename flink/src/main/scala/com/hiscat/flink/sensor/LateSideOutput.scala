package com.hiscat.flink.sensor

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object LateSideOutput {
  def main(args: Array[String]): Unit = {
    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val stream = environment.socketTextStream("localhost", 10000)
      .map(_.split(" "))
      .map(e => (e(0), e(1).toLong * 1000))
      .assignAscendingTimestamps(_._2)
      .keyBy(_._1)
      .process(new KeyedProcessFunction[String, (String, Long), (String, Long)] {

        override def processElement(value: (String, Long),
                                    ctx: KeyedProcessFunction[String, (String, Long), (String, Long)]#Context,
                                    out: Collector[(String, Long)]): Unit = {
          if (value._2 < ctx.timerService().currentWatermark()) {
            ctx.output(OutputTag[(String, Long)]("late"), value)
          } else {
            out.collect(value)
          }

        }
      })


    stream.getSideOutput(OutputTag[(String, Long)]("late"))
      .print()
    environment.execute()
  }
}
