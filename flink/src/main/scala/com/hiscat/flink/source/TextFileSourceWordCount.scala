package com.hiscat.flink.source

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object TextFileSourceWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.readTextFile("input/word.txt")
      .flatMap(_.toLowerCase.split("\\W+").filter(_.nonEmpty))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .print()

    env.execute("TextFileWordCount")
  }

}
