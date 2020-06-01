package com.hiscat.flink.state

import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object MapStateTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env
      .socketTextStream("localhost", 8001)
      .flatMap(_.split(" "))
      .map((_, 1L))
      .keyBy(_._1)
      .process(new KeyedProcessFunction[String, (String, Long), (String, Long)] {

        lazy val words: MapState[String, Long] =
          getRuntimeContext.getMapState(new MapStateDescriptor[String, Long]("words", classOf[String], classOf[Long]))

        override def processElement(value: (String, Long), ctx: KeyedProcessFunction[String, (String, Long), (String, Long)]#Context, out: Collector[(String, Long)]): Unit = {
          if (!words.contains(value._1)) {
            words.put(value._1, 1)
          } else {
            words.put(value._1, words.get(value._1) + value._2)
          }
          out.collect((value._1, words.get(value._1)))
        }
      })
      .print()

    env.execute()
  }
}
