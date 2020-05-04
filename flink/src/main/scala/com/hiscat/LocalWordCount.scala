package com.hiscat

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

object LocalWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.socketTextStream("localhost", 9000)
      .flatMap(_.split(" "))
      .map(WordWithCount(_, 1))
      .keyBy("word")
      .sum("count")
      .print()

    env.execute("LocalWordCount")
  }

  case class WordWithCount(word: String, count: Long)

}
