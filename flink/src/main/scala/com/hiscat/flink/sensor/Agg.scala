package com.hiscat.flink.sensor

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object Agg {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.addSource(new SensorTest.SensorSource)
      .map(t => (t.id, t.temperature))
      .keyBy(0)
      .timeWindow(Time.seconds(20))
      .aggregate(new AvgTemp)
      .print()

    env.execute()
  }


  class AvgTemp extends AggregateFunction[(String, Double), (String, Double, Double), (String, Double)] {
    override def createAccumulator(): (String, Double, Double) = ("", 0, 0)

    override def add(value: (String, Double), accumulator: (String, Double, Double)): (String, Double, Double) = {
      (value._1, accumulator._2 + value._2, accumulator._3 + 1)
    }

    override def getResult(accumulator: (String, Double, Double)): (String, Double) = (accumulator._1, accumulator._2 / accumulator._3)

    override def merge(a: (String, Double, Double), b: (String, Double, Double)): (String, Double, Double) = (a._1, a._2 + b._2, a._3 + b._3)
  }

}
