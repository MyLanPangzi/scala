package com.hiscat.flink.state

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.common.typeinfo.{TypeHint, TypeInformation}
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction.Context

import scala.collection.mutable.ListBuffer

object BufferingSinkTest {

  class BufferingSink(threshold: Int = 0) extends SinkFunction[(String, Int)] with CheckpointedFunction {

    @transient
    private var checkpointedState: ListState[(String, Int)] = _

    private val bufferedElements = ListBuffer[(String, Int)]()

    def invoke(value: (String, Int), context: Context[(String, Int)]): Unit = {
      bufferedElements += value
      if (bufferedElements.size == threshold) {
        for (element <- bufferedElements) {
          // send it to the sink
        }
        bufferedElements.clear()
      }
    }

    override def snapshotState(context: FunctionSnapshotContext): Unit = {
      checkpointedState.clear()
      for (element <- bufferedElements) {
        checkpointedState.add(element)
      }
    }

    override def initializeState(context: FunctionInitializationContext): Unit = {
      val descriptor = new ListStateDescriptor[(String, Int)](
        "buffered-elements",
        TypeInformation.of(new TypeHint[(String, Int)]() {})
      )

      checkpointedState = context.getOperatorStateStore.getListState(descriptor)

      if (context.isRestored) {
        import scala.collection.JavaConversions._
        checkpointedState.get().foreach((e: (String, Int)) => {
          bufferedElements += e
        })
      }
    }

  }

}
