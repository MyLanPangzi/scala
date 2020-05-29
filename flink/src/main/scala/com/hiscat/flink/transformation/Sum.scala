package com.hiscat.flink.transformation

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Sum {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.fromElements(("hello", 3), ("hello", 2), ("hello", 3), ("world", 2), ("world", 3), ("flink", 0), ("flink", 1))
      .keyBy(0)
      .sum(1)
      .print()

    env.execute()
  }
}
