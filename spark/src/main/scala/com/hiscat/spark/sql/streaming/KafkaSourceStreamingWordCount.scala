package com.hiscat.spark.sql.streaming

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object KafkaSourceStreamingWordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("KafkaSourceStreamingWordCount")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "test")
      .load()
      .selectExpr("cast(value as string)")

      .as[String]
      .flatMap(_.split(" "))
      .groupBy("value")
      .count()

      .writeStream
      .outputMode(OutputMode.Update())
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
