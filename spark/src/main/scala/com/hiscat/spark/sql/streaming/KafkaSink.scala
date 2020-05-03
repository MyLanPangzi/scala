package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object KafkaSink {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("FileSink")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

      .as[String]
      .flatMap(_.split(" "))
      .toDF("value")

      .writeStream
      .outputMode(OutputMode.Append())
      .option("kafka.bootstrap.servers","localhost:9092")
      .option("checkpointLocation","output/kafkasink")
      .option("topic","kafkasink")
      .format("kafka")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
