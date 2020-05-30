package com.hiscat.flink.sensor

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object MaxMinTempProcess {

  case class MaxMin(id: String, min: Double, max: Double, end: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.addSource(new SensorTest.SensorSource)
      .keyBy(_.id)
      .timeWindow(Time.seconds(10))
      .process(new MaxMinFunc)
      .print()

    env.execute()
  }

  class MaxMinFunc extends ProcessWindowFunction[SensorReading, MaxMin, String, TimeWindow] {


    override def process(key: String, context: Context, elements: Iterable[SensorReading], out: Collector[MaxMin]): Unit = {

      val temp = elements.map(_.temperature)
      val result = MaxMin(key, temp.min, temp.max, context.window.getEnd)

      out.collect(result)
    }
  }

}
