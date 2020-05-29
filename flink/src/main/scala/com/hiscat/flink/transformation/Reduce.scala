package com.hiscat.flink.transformation

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object Reduce {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.fromElements(("hello", 8), ("hello", 2), ("hello", 3), ("world", 2), ("world", 3), ("flink", 0), ("flink", 1))
      .keyBy(0)
      .reduce((x, y) => (x._1, x._2 + y._2))
      .print()

    env.execute()
  }
}
