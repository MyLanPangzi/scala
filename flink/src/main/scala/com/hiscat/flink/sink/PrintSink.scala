package com.hiscat.flink.sink

import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object PrintSink {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromElements("hello", "world")
      .addSink(new PrintSinkFunction[String]())

    env.execute("PrintSink")
  }
}
