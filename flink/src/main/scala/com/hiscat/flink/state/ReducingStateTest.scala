package com.hiscat.flink.state

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ReducingStateTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env
      .socketTextStream("localhost", 8002)
      .flatMap(_.split(" "))
      .map((_, 1L))
      .keyBy(_._1)
      .process(new KeyedProcessFunction[String, (String, Long), (String, Long)] {

        lazy val words: ReducingState[(String, Long)] =
          getRuntimeContext.getReducingState(new ReducingStateDescriptor[(String, Long)](
            "words",
            new ReduceFunction[(String, Long)] {
              override def reduce(value1: (String, Long), value2: (String, Long)): (String, Long) = {
                (value1._1, value1._2 + value2._2)
              }
            },
            classOf[(String, Long)]))

        override def processElement(value: (String, Long),
                                    ctx: KeyedProcessFunction[String, (String, Long), (String, Long)]#Context,
                                    out: Collector[(String, Long)]): Unit = {
          words.add(value)
          out.collect(words.get())
        }
      })
      .print()


    env.execute()
  }
}
