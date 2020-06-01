package com.hiscat.flink.state

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{AggregatingState, AggregatingStateDescriptor}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object AggregateStateTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env
      .socketTextStream("localhost", 8003)
      .flatMap(_.split(" "))
      .map((_, 1L))
      .keyBy(_._1)
      .process(new KeyedProcessFunction[String, (String, Long), (String, Long)] {

        lazy val words: AggregatingState[(String, Long), (String, Long)] =
          getRuntimeContext.getAggregatingState(new AggregatingStateDescriptor[(String, Long), (String, Long), (String, Long)](
            "words",
            new AggregateFunction[(String, Long), (String, Long), (String, Long)] {
              override def createAccumulator(): (String, Long) = ("", 0)

              override def add(value: (String, Long), accumulator: (String, Long)): (String, Long) = (value._1, value._2 + accumulator._2)

              override def getResult(accumulator: (String, Long)): (String, Long) = accumulator

              override def merge(a: (String, Long), b: (String, Long)): (String, Long) = (a._1, a._2 + b._2)
            },
            classOf[(String, Long)]
          ))

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
