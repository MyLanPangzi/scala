package com.hiscat.flink.state

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{StateTtlConfig, ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.time.Time
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object TtlValueStateTest {

  class CountWindowAverage extends RichFlatMapFunction[(Long, Long), (Long, Long)] {
    var sum: ValueState[(Long, Long)] = _

    override def flatMap(input: (Long, Long), out: Collector[(Long, Long)]): Unit = {
      val tmpValue = sum.value()
      val current = if (tmpValue != null) {
        tmpValue
      } else {
        (0L, 0L)
      }
      val newSum = (current._1 + 1, current._2 + input._2)
      sum.update(newSum)
      Thread.sleep(1000)
      println(newSum)
      //never reach
      if (newSum._1 >= 2) {
        out.collect(input._1, newSum._2 / newSum._1)
        sum.clear()
      }
    }

    override def open(parameters: Configuration): Unit = {
      val ttlConfig = StateTtlConfig
        .newBuilder(Time.seconds(1))
        .disableCleanupInBackground()
        .updateTtlOnCreateAndWrite()
        .neverReturnExpired()
//        .cleanupFullSnapshot()
//        .cleanupIncrementally(10,true)
        .cleanupInRocksdbCompactFilter(1000)
        .build()
      val descriptor = new ValueStateDescriptor[(Long, Long)]("sum", createTypeInformation[(Long, Long)])
      descriptor.enableTimeToLive(ttlConfig)
      sum = getRuntimeContext.getState(descriptor)
    }
  }

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromCollection(List(
      (1L, 3L),
      (1L, 6L),
      (1L, 7L),
      (1L, 4L),
      (1L, 2L)
    ))
      .keyBy(_._1)
      .flatMap(new CountWindowAverage)
      .print()

    env.execute("TtlValueStateTest")
  }
}
