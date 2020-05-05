package com.hiscat.flink.source

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object GenerateSequence {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env
      .generateSequence(0, 10)
      .iterate(it => (it.filter(_ % 2 == 0), it.filter(_ % 2 != 0)))
      .print()

    env.execute("GenerateSequence")
  }
}
