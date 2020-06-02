package com.hiscat.flink.sink

import java.lang
import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, KafkaSerializationSchema}
import org.apache.kafka.clients.producer.ProducerRecord

object KafkaTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "hadoop102:9092,hadoop104:9092,hadoop104:9092")
    properties.setProperty("group.id", "test")
    env
      .addSource(new FlinkKafkaConsumer[String]("words", new SimpleStringSchema(), properties))
      .map(_.toUpperCase)
      .addSink(new FlinkKafkaProducer[String]("count", new KafkaSerializationSchema[String] {
        override def serialize(element: String, timestamp: lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
          new ProducerRecord[Array[Byte], Array[Byte]]("count", element.getBytes())
        }
      }, properties, Semantic.EXACTLY_ONCE))

    env.execute()
  }
}
