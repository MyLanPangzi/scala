package com.hiscat.flink.source

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object ElementsSourceWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //    env.fromCollection(Seq("hello world", "hello flink"))
    env.fromElements("hello world", "hello flink")
      .flatMap(_.toLowerCase.split("\\W+").filter(_.nonEmpty))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .print()

    env.execute("ElementsWordCount")
  }

}
