package com.hiscat.flink.state

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._

object ValueStateTest {

  class CountWindowAverage extends RichFlatMapFunction[(Long, Long), (Long, Long)] {
    var sum: ValueState[(Long, Long)] = _

    override def flatMap(input: (Long, Long), out: Collector[(Long, Long)]): Unit = {
      val tmp = sum.value()
      val current = if (tmp != null) {
        tmp
      } else {
        (0L, 0L)
      }
      val newSum = (current._1 + 1, current._2 + input._2)
      sum.update(newSum)
      if (newSum._1 >= 2) {
        out.collect(input._1, newSum._2 / newSum._1)
        sum.clear()
      }
    }

    override def open(parameters: Configuration): Unit = {
      sum = getRuntimeContext.getState(
        new ValueStateDescriptor[(Long, Long)]("sum", createTypeInformation[(Long, Long)])
      )
    }
  }

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromCollection(List(
      (1L, 3L),
      (1L, 5L),
      (1L, 7L),
      (1L, 4L),
      (1L, 2L)
    ))
        .keyBy(_._1)
        .flatMap(new CountWindowAverage)
        .print()

    env.execute("ValueStateTest")
  }
}
