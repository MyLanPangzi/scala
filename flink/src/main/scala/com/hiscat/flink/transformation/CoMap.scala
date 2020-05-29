package com.hiscat.flink.transformation

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object CoMap {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val one = env.fromElements(("hello", 5), ("hi", 2), ("Bye", 3))
    val two = env.fromElements(("hello", 1), ("hi", 2), ("Bye", 4))
    one.keyBy(0)
      .connect(two.keyBy(0))
      .map(new CoMapFunc)
      .keyBy(_._1)
      .sum(2)
      .print()

    env.execute()
  }

  class CoMapFunc extends RichCoMapFunction[(String, Int), (String, Int), (String, Int, Int)] {

    override def map1(value: (String, Int)): (String, Int, Int) = {
      (value._1, value._2, 0)
    }


    override def map2(value: (String, Int)): (String, Int, Int) = {
      (value._1, 0, value._2)
    }
  }

}
