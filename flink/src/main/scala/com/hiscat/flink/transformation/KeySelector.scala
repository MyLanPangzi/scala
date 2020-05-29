package com.hiscat.flink.transformation

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object KeySelector {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromElements((1, 1), (1, 2), (2, 3), (2, 4))
//      .keyBy(0)
//      .keyBy("_1")
      .keyBy(_._1)
      .print()

    env.execute()
  }

}
