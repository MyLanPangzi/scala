package com.hiscat.spark.streaming

import java.time.{Instant, ZoneOffset}

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DirectKafkaWordCount {
  def main(args: Array[String]): Unit = {

    val ssc = new StreamingContext(new SparkConf().setAppName("DirectKafkaWordCount").setMaster("local[*]"), Seconds(2))

    // Create direct kafka stream with brokers and topics
    val Array(brokers, groupId, topics) = Array("localhost:9092", "test", "test")
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])

    KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams)
    )
      .map(_.value)
      .flatMap(_.split(" "))
      .map(x => (x, 1L))
      .reduceByKey(_ + _)
      .foreachRDD((rdd, time) => {
        println(Instant.ofEpochMilli(time.milliseconds).atOffset(ZoneOffset.ofHours(8)).toLocalDateTime)
        println(rdd.collect().mkString(","))
      })
    //      .print()

//    KafkaUtils.createDirectStream(ssc,LocationStrategies.PreferBrokers)

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}
