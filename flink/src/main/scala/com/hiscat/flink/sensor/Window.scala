package com.hiscat.flink.sensor

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object Window {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.addSource(new SensorTest.SensorSource)
      .map(t => (t.id, t.temperature))
      .keyBy(_._1)
      .timeWindow(Time.seconds(10), Time.seconds(5))
      .min(1)
      .print()

    env.execute()
  }
}
