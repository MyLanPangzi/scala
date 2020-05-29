package com.hiscat.flink.sensor

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object Process {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env
      .addSource(new SensorTest.SensorSource)
      .map(t => (t.id, t.temperature))
      .keyBy(_._1)
      .timeWindow(Time.seconds(10))
      .process(new ProcessFunc)
      .print()

    env.execute()
  }


  class ProcessFunc extends ProcessWindowFunction[(String, Double), (String, Double), String, TimeWindow] {
    override def process(key: String, context: Context, elements: Iterable[(String, Double)], out: Collector[(String, Double)]): Unit = {
      out.collect((key, elements.map(_._2).sum / elements.size))
    }
  }

}
