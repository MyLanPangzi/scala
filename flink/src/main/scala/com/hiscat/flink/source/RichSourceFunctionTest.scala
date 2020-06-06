package com.hiscat.flink.source

import java.time.{Instant, LocalDateTime, ZoneId}

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object RichSourceFunctionTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.addSource(new MarketingUserBehaviorSource)
      .assignAscendingTimestamps(_.ts)
      .map(e => (e.channel, e.behavior, 1L))
      .keyBy(e => (e._1, e._2))
      .timeWindow(Time.seconds(10), Time.seconds(2))
      .aggregate(new CountByChannel,
        (_, w, it: Iterable[(String, String, Long)], out: Collector[String]) =>
          out.collect(
            s"""
               |w:${LocalDateTime.ofInstant(Instant.ofEpochMilli(w.getEnd), ZoneId.systemDefault())},${it.head}
               |""".stripMargin)
      )
      .print()

    env.execute("RichSourceFunctionTest")
  }

  class CountByChannel extends AggregateFunction[(String, String, Long), (String, String, Long), (String, String, Long)] {
    override def createAccumulator(): (String, String, Long) = ("", "", 0L)

    override def add(value: (String, String, Long), accumulator: (String, String, Long)): (String, String, Long) = (value._1, value._2, accumulator._3 + 1)

    override def getResult(accumulator: (String, String, Long)): (String, String, Long) = accumulator

    override def merge(a: (String, String, Long), b: (String, String, Long)): (String, String, Long) = (a._1, a._2, a._3 + b._3)
  }

}
