package com.hiscat.flink.transformation

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Max {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.fromElements(("hello", 4), ("hello", 2), ("hello", 3), ("world", 2), ("world", 3), ("flink", 0), ("flink", 1))
      .keyBy(0)
//      .max(1)
      .maxBy(1)
      .print()

    env.execute()
  }
}
