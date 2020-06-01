package com.hiscat.flink.sensor

import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object AllowedLateness {
  def main(args: Array[String]): Unit = {
    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val stream = environment.socketTextStream("localhost", 10001)
      .map(_.split(" "))
      .map(e => (e(0), e(1).toLong * 1000))
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[(String, Long)](Time.seconds(10)) {
        override def extractTimestamp(element: (String, Long)): Long = element._2
      })
      .keyBy(_._1)
      .timeWindow(Time.seconds(10))
      .allowedLateness(Time.seconds(10))
      .sideOutputLateData(OutputTag[(String, Long)]("late"))
      .process(new ProcessWindowFunc)

    stream.print()

    stream.getSideOutput(OutputTag[(String, Long)]("late"))
      .print()
    environment.execute()
  }

  class ProcessWindowFunc extends ProcessWindowFunction[(String, Long), String, String, TimeWindow] {
    override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {
      val first = context.windowState.getState(new ValueStateDescriptor[Boolean]("first", Types.of[Boolean]))
      val count = context.windowState.getState(new ValueStateDescriptor[Long]("count", Types.of[Long]))
      if (!first.value()) {
        first.update(true)
        count.update(elements.size)
        out.collect(s"k:$key,count:${count.value()}")
      }else{
        out.collect(s"k:$key,count:${elements.size},late:${elements.size - count.value()}")
      }
    }
  }

}
