package com.hiscat.flink.source

import java.sql.Timestamp
import java.time.{Instant, LocalDateTime, ZoneId}

import com.hiscat.flink.source.RichSourceFunctionTest.CountByChannel
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object RichSourceFunctionTest2 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.addSource(new MarketingUserBehaviorSource)
      .assignAscendingTimestamps(_.ts)
      .filter(_.behavior != "uninstall")
      .timeWindowAll(Time.seconds(10), Time.seconds(2))
      .process(new CountAll)
      .print()

    env.execute("RichSourceFunctionTest2")
  }


  class CountAll extends ProcessAllWindowFunction[MarketingUserBehavior, String, TimeWindow] {
    override def process(context: Context, elements: Iterable[MarketingUserBehavior], out: Collector[String]): Unit =
      out.collect(
        s"""
           |w:${new Timestamp(context.window.getEnd)},size:${elements.size}
           |""".stripMargin)
  }

}
