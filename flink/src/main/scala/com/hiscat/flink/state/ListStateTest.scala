package com.hiscat.flink.state

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ListStateTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

        env
          .socketTextStream("localhost", 8000)
          .flatMap(_.split(" "))
          .map((_, 1))
          .keyBy(_._1)
          .process(new KeyedProcessFunction[String, (String, Int), (String, Long)] {

            lazy val words: ListState[String] =
              getRuntimeContext.getListState(new ListStateDescriptor[String]("words", Types.of[String]))

            override def processElement(value: (String, Int),
                                        ctx: KeyedProcessFunction[String, (String, Int), (String, Long)]#Context,
                                        out: Collector[(String, Long)]): Unit = {
              import scala.collection.JavaConversions._
              words.add(value._1)
              out.collect((value._1, words.get().size))
            }
          })
          .print()

//    env.fromElements("hello world")
//      .flatMap(_.split(" "))
//      .map((_, 1L))
//      .process(new ProcessFunction[(String, Long), (String, Long)] {
//        lazy val words: ListState[String] =
//          getRuntimeContext.getListState(new ListStateDescriptor[String]("words", Types.of[String]))
//
//        override def processElement(value: (String, Long), ctx: ProcessFunction[(String, Long), (String, Long)]#Context, out: Collector[(String, Long)]): Unit = {
//          import scala.collection.JavaConversions._
//          words.add(value._1)
//          out.collect((value._1, words.get().size))
//        }
//      })
//      .print()

    env.execute()
  }
}
