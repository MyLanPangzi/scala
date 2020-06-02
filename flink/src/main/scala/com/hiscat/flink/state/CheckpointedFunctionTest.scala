package com.hiscat.flink.state

import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object CheckpointedFunctionTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
//    env.setParallelism(1)

    env.socketTextStream("localhost", 9003)
      .flatMap(_.split(" "))
      .map((_, 1L))
      .process(new WordCountProcessFunc)
      .print()

    env.execute()
  }

  class WordCountProcessFunc extends ProcessFunction[(String, Long), (String, Long)] with CheckpointedFunction {


    override def processElement(value: (String, Long),
                                ctx: ProcessFunction[(String, Long), (String, Long)]#Context,
                                out: Collector[(String, Long)]): Unit = {
      //noinspection DuplicatedCode
//      if (!words.contains(value._1)) {
//        words.put(value._1, 1)
//      } else {
//        words.put(value._1, words.get(value._1) + value._2)
//      }
      out.collect((value._1, words.get(value._1)))
    }

    override def snapshotState(context: FunctionSnapshotContext): Unit = {

    }

    var words: BroadcastState[String, Long] = _

    override def initializeState(context: FunctionInitializationContext): Unit = {
//      context.getOperatorStateStore.getUnionListState()
//      words = context.getOperatorStateStore.getBroadcastState(
//        new MapStateDescriptor[String, Long]("words", classOf[String], classOf[Long]))

    }
  }

}
