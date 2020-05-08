package com.hiscat.flink.state

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object MapWithStateTest {


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromElements("hello world", "hello scala", "hello flink")
      .flatMap(_.split(" "))
      .map((_, 1))
      .keyBy(0)
      .mapWithState((in: (String, Int), count: Option[Int]) => {
        count match {
          case Some(x) => ((in._1, x + in._2), Some(x + in._2))
          case None => ((in._1, 1), Some(in._2))
        }
      })
      .print()

    env.execute("ValueStateTest")
  }
}
