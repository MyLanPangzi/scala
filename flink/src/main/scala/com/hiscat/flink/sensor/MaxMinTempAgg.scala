package com.hiscat.flink.sensor

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object MaxMinTempAgg {

  case class MaxMin(id: String, min: Double, max: Double, end: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.addSource(new SensorTest.SensorSource)

      .keyBy(_.id)
      .timeWindow(Time.seconds(10))
      .aggregate(
        new MaxMinAgg,
        (k: String, w: TimeWindow, it: Iterable[MaxMin], out: Collector[MaxMin]) =>
          out.collect(MaxMin(k, it.head.min, it.head.max, w.getEnd))
      )
      .print()

    env.execute()
  }

  class MaxMinAgg extends AggregateFunction[SensorReading, MaxMin, MaxMin] {
    override def createAccumulator(): MaxMin = MaxMin("", Double.MaxValue, Double.MinValue, 0)

    override def add(value: SensorReading, accumulator: MaxMin): MaxMin = {

      MaxMin(accumulator.id, accumulator.min.min(value.temperature), accumulator.max.max(value.temperature), accumulator.end)
    }

    override def getResult(accumulator: MaxMin): MaxMin = accumulator

    override def merge(a: MaxMin, b: MaxMin): MaxMin = {
      MaxMin(a.id, a.min.min(b.min), a.max.max(b.max), a.end)
    }
  }

}
