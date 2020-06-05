package com.hiscat.flink.ecommerce

import java.time.Duration
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

object KafkaConsumerTest {
  def main(args: Array[String]): Unit = {
    val properties = new Properties()
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "items.test.group")

    val consumer = new KafkaConsumer[String, String](properties)
    import scala.collection.JavaConversions._
    consumer.subscribe(List("items"))

    consumer.poll(Duration.ofMinutes(1))
      .records("items")
      .foreach(r => {
        println(r.value())
      })

    consumer.close()
  }
}
