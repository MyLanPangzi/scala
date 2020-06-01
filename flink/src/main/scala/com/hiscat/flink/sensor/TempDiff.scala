package com.hiscat.flink.sensor

import com.hiscat.flink.sensor.MaxMinTempAgg.MaxMinAgg
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object TempDiff {

  case class MaxMin(id: String, min: Double, max: Double, end: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.addSource(new SensorTest.SensorSource)
      .keyBy(_.id)
      .process(new TempDiffProcessFunc(2.0))
      .print()

    env.execute()
  }


  class TempDiffProcessFunc(threshold: Double) extends KeyedProcessFunction[String, SensorReading, SensorReading] {
    lazy val last: ValueState[Double] =
      getRuntimeContext.getState(new ValueStateDescriptor[Double]("last", classOf[Double]))

    lazy val timer: ValueState[Long] =
      getRuntimeContext.getState(new ValueStateDescriptor[Long]("timer", classOf[Long]))

    override def processElement(value: SensorReading, ctx: KeyedProcessFunction[String, SensorReading, SensorReading]#Context, out: Collector[SensorReading]): Unit = {
      ctx.timerService().deleteEventTimeTimer(timer.value())
      timer.update(ctx.timerService().currentProcessingTime() + 60 * 60 * 1000)
      ctx.timerService().registerProcessingTimeTimer(timer.value())

      if ((value.temperature - last.value()).abs > threshold) {
        out.collect(value)
      }
      last.update(value.temperature)

    }

    override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, SensorReading, SensorReading]#OnTimerContext, out: Collector[SensorReading]): Unit = {
      last.clear()
      timer.clear()
    }
  }

}
