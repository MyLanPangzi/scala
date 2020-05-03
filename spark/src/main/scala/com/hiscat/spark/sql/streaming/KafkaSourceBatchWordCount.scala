package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object KafkaSourceBatchWordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("KafkaSourceBatchWordCount")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.read
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "test")
      .option("startingOffsets", """{"test":{"0":10}} """)
      .option("endingOffsets", """latest""")
      .load()
      .selectExpr("cast(value as string)")

      .as[String]
      .flatMap(_.split(" "))
      .groupBy("value")
      .count()

      .write
      .format("console")
      .save()

    spark.stop()
  }
}
