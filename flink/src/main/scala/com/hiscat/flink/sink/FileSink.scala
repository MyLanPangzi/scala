package com.hiscat.flink.sink

import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object FileSink {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val sink = StreamingFileSink.forRowFormat(
      new Path("output/people"),
      new SimpleStringEncoder[String]())
      .build()

    env.fromElements("hello", "world")
      .addSink(sink)

    env.execute("Sink")
  }
}
