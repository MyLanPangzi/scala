package com.hiscat

import org.apache.flink.streaming.api.scala._


object WordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.fromCollection(List("hello world"))
      .flatMap(_.toLowerCase().split(" "))
      .filter(_.nonEmpty)
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .print()
    env.execute("Streaming WordCount")
  }
}
