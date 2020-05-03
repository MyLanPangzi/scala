package com.hiscat.spark.sql.streaming

import java.sql.Timestamp

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

object WatermarkUpdateMode {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("WatermarkUpdateMode")
      .master("local[*]")
      .getOrCreate()

    import org.apache.spark.sql.functions._
    import spark.implicits._

    spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

      .as[String]
      .map(e => {
        val s = e.split(",")
        (s(1), Timestamp.valueOf(s(0)))
      })
      .toDF("word", "ts")
      .withWatermark("ts", "2 minutes")
      .groupBy(
        window($"ts", "10 minutes", "2 minutes"),
        $"word"
      )

      .count()
      .writeStream
      .outputMode(OutputMode.Update())
      .option("truncate", value = false)
      .format("console")
      .trigger(Trigger.ProcessingTime("1 seconds"))
      .start()
      .awaitTermination()

    spark.stop()
  }
}
