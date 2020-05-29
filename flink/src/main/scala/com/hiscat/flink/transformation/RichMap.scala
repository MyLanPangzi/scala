package com.hiscat.flink.transformation

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object RichMap {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.fromElements("hello", "world")
      .map(new RichMapFunction[String,String] {

        override def open(parameters: Configuration): Unit = {
          println("open")
        }

        override def map(value: String): String = value.toUpperCase

        override def close(): Unit = {
          println("close")
        }
      })
        .print()
    env.execute()
  }
}
