package com.hiscat.flink.ecommerce

import java.nio.file.{Files, Paths}
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

object KafkaProducerTest {
  def main(args: Array[String]): Unit = {
    val properties = new Properties
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](properties)

    import scala.collection.JavaConversions._
    Files.readAllLines(Paths.get("E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\UserBehavior.csv"))
      .foreach(e => producer.send(new ProducerRecord[String, String]("items", e)))

    producer.close()
  }
}
