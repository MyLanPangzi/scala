package com.hiscat.flink.ecommerce

import java.sql.Timestamp
import java.time.{Instant, ZoneId}

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{ProcessAllWindowFunction, ProcessWindowFunction}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object Uv {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env
      .readTextFile("E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\UserBehavior.csv")
      .map(_.split(","))
      .map(strings => {
        UserBehavior(
          userId = strings(0).toLong,
          itemId = strings(1).toLong,
          categoryId = strings(2).toInt,
          behavior = strings(3),
          timestamp = strings(4).toLong
        )
      })
      .filter(_.behavior == "pv")
      .assignAscendingTimestamps(_.timestamp * 1000)
      .timeWindowAll(Time.hours(1))
      .process(new UvProcess)
      .print()


    env.execute()
  }

  class UvProcess extends ProcessAllWindowFunction[UserBehavior, String, TimeWindow] {
    override def process(context: Context, elements: Iterable[UserBehavior], out: Collector[String]): Unit = {
      out.collect(s"w:${Instant.ofEpochMilli(context.window.getEnd).atZone(ZoneId.systemDefault()).toLocalDateTime}," +
        s" uv:${elements.map(_.userId).toSet.size}")
    }
  }

}
