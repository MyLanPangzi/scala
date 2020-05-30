package com.hiscat.flink.time

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object EventTime {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    env.getConfig.setAutoWatermarkInterval(6000)

    env.socketTextStream("localhost", 9000)
      .map(_.split(" "))
      .map(arr => (arr(0), arr(1).toLong * 1000))

      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[(String, Long)](Time.seconds(5)) {
        override def extractTimestamp(element: (String, Long)): Long = {
          element._2
        }
      })
      .keyBy(_._1)
      .timeWindow(Time.seconds(10))
      .apply(
        (k, w, it, out: Collector[String]) =>
          out.collect(s"k:$k,window:${w.getEnd},count:${it.size}")
      )
      .print()

    env.execute()
  }

  class EventTimeProcessFunc extends ProcessWindowFunction[(String, Long), String, String, TimeWindow] {
    override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {
      out.collect(s"k:$key window end:${context.window.getEnd}, window count:${elements.size}")
    }
  }

}
