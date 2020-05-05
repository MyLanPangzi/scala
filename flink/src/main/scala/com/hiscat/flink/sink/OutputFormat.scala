package com.hiscat.flink.sink

import org.apache.flink.api.scala.operators.ScalaCsvOutputFormat
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object OutputFormat {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromElements(("hello" -> "world"))
      .writeUsingOutputFormat(new ScalaCsvOutputFormat(new Path("output/csv")))

    env.execute("Sink")
  }
}
