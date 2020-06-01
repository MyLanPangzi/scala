package com.hiscat.flink.sensor

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.Trigger.TriggerContext
import org.apache.flink.streaming.api.windowing.triggers._
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object Trigger {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.addSource(new SensorSource)
      .assignAscendingTimestamps(_.timestamp)
      .keyBy(_.id)
      .timeWindow(Time.seconds(10))
      .trigger(new OneSecondTrigger)

      .process(new ProcessWindowFunction[SensorReading, String, String, TimeWindow] {
        override def process(key: String, context: Context, elements: Iterable[SensorReading], out: Collector[String]): Unit = {
          out.collect(s"k:$key,w:${context.window.getEnd}, count:${elements.size}")
        }
      })
      .print()
    env.execute()
  }

  class OneSecondTrigger extends Trigger[SensorReading, TimeWindow] {
    override def onElement(element: SensorReading, timestamp: Long, window: TimeWindow, ctx: TriggerContext): TriggerResult = {
      val first = ctx.getPartitionedState(new ValueStateDescriptor[Boolean]("first", classOf[Boolean]))
      if (!first.value()) {
        {
          ctx.registerEventTimeTimer(ctx.getCurrentWatermark + 1000 - ctx.getCurrentWatermark % 1000)
          ctx.registerEventTimeTimer(window.getEnd)
        }
        first.update(true)
      }
      TriggerResult.CONTINUE
    }

    override def onProcessingTime(time: Long, window: TimeWindow, ctx: TriggerContext): TriggerResult = TriggerResult.CONTINUE

    override def onEventTime(time: Long, window: TimeWindow, ctx: TriggerContext): TriggerResult = {
      if (window.getEnd == time) {
        TriggerResult.FIRE_AND_PURGE
      } else {
        val t = ctx.getCurrentWatermark + 1000 - ctx.getCurrentWatermark % 1000
        if (t < window.getEnd) {
          ctx.registerEventTimeTimer(t)
        }
      }
      TriggerResult.FIRE
    }

    override def clear(window: TimeWindow, ctx: TriggerContext): Unit = {
      ctx.getPartitionedState(new ValueStateDescriptor[Boolean]("first", classOf[Boolean])).clear()
    }
  }

}
