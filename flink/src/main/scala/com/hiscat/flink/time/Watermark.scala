package com.hiscat.flink.time

import com.hiscat.flink.sensor.SensorReading
import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object Watermark {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.addSource(new SensorSource)
            .assignAscendingTimestamps(_.timestamp)
      //      .assignTimestampsAndWatermarks(new AscendingTimestampExtractor[SensorReading] {
      //        override def extractAscendingTimestamp(element: SensorReading): Long = element.timestamp
      //      })
      //      .assignTimestampsAndWatermarks(new AssignerWithWatermarksImpl(Time.seconds(5).toMilliseconds))
      //      .assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks[SensorReading] {
      //        override def checkAndGetNextWatermark(lastElement: SensorReading, extractedTimestamp: Long): Watermark = {
      //          if (lastElement.id == "sensor_1") {
      //            new Watermark(extractedTimestamp)
      //          }
      //          null
      //        }
      //
      //        override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = element.timestamp
      //      })
      .keyBy(_.id)
      .timeWindow(Time.seconds(1))
      .apply(
        (k, w, it, out: Collector[String]) =>
          out.collect(s"k:$k,window:${w.getEnd},count:${it.last.temperature}")
      )
      .print()

    env.execute()
  }

  class AssignerWithWatermarksImpl(time: Long) extends AssignerWithPeriodicWatermarks[SensorReading] {
    private var maxTimestamp = 0L

    override def getCurrentWatermark: Watermark = new Watermark(maxTimestamp - time)

    override def extractTimestamp(element: SensorReading, previousElementTimestamp: Long): Long = {
      maxTimestamp = maxTimestamp.max(element.timestamp)
      element.timestamp
    }
  }

}
