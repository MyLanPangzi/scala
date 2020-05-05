package com.hiscat.flink.sink

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010

object KafkaSink {


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val sink = new FlinkKafkaProducer010[String]("localhost:9092", "flink", new SimpleStringSchema())
    env.fromElements("hello", "world")
      .addSink(sink)

    env.execute("Sink")
  }
}
