package com.hiscat.flink.sensor

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object MaxMinTempReduce {

  case class MaxMin(id: String, min: Double, max: Double, end: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.addSource(new SensorTest.SensorSource)
      .map(r => (r.id, r.temperature, r.temperature))
      .keyBy(_._1)
      .timeWindow(Time.seconds(10))
      .reduce(
        (x, y) => (x._1, x._2.min(y._2), x._3.max(y._3)),
        (k, w, it, out: Collector[MaxMin]) =>
          out.collect(MaxMin(k, it.head._2, it.head._3, w.getEnd))
      )
      .print()

    env.execute()
  }

}
