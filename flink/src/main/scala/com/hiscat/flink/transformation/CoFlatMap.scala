package com.hiscat.flink.transformation

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.co.{RichCoFlatMapFunction, RichCoMapFunction}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

object CoFlatMap {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val one = env.fromElements(("hello", 5), ("hi", 2), ("Bye", 3))
    val two = env.fromElements(("hello", 1), ("hi", 2), ("Bye", 3))
    one.keyBy(0)
      .connect(two.keyBy(0))
      .flatMap(new FlatCoMapFunc)
      .print()

    env.execute()
  }

  class FlatCoMapFunc extends RichCoFlatMapFunction[(String, Int), (String, Int), (String, Int, Int)] {

    lazy val lengthState: ValueState[(String, Int)] =
      getRuntimeContext.getState(new ValueStateDescriptor[(String, Int)]("lengthState", classOf[(String, Int)]))
    lazy val countState: ValueState[(String, Int)] =
      getRuntimeContext.getState(new ValueStateDescriptor[(String, Int)]("countState", classOf[(String, Int)]))

    override def flatMap1(value: (String, Int), out: Collector[(String, Int, Int)]): Unit = {
      if (lengthState.value() == null) {
        lengthState.update(value)
      }
      if (countState.value() != null) {
        out.collect((value._1, value._2, countState.value()._2))
        countState.clear()
        lengthState.clear()
      }
    }

    override def flatMap2(value: (String, Int), out: Collector[(String, Int, Int)]): Unit = {
      if (countState.value() == null) {
        countState.update(value)
      }
      if (lengthState.value() != null) {
        out.collect((value._1, lengthState.value()._2, value._2))
        countState.clear()
        lengthState.clear()
      }
    }

  }

}
