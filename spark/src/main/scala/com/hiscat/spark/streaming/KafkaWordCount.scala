package com.hiscat.spark.streaming

import java.time.{Instant, ZoneOffset}

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object KafkaWordCount {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(
      new SparkConf().setMaster("local[*]").setAppName("KafkaWordCount"),
      Seconds(5)
    )
    KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](
        topics = Set("test"),
        kafkaParams = mutable.Map[String,Object](
          ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
          ConsumerConfig.GROUP_ID_CONFIG -> "test",
          ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
          ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
        )
      )
    )
      .map(_.value)
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .foreachRDD((rdd, t) => {
        println(Instant.ofEpochMilli(t.milliseconds).atOffset(ZoneOffset.ofHours(8)))
        rdd.foreachPartition(_.foreach(println))
        println()
      })

    ssc.start()
    ssc.awaitTermination()
  }
}
