package com.hiscat.spark.sql.streaming

import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoField, TemporalField}

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.streaming.Durations

import scala.concurrent.duration.Duration

object WindowSpeculate {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("WindowSpeculate")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql.functions._

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
      .groupBy(
        window($"ts", "10 minutes", "3 minutes"),
        $"word"
      )
      //      2020-05-03 06:17:00,hello
      //
      //[6:00-16:00) x
      //[9:00-19:00)
      //[12:00-22:00)
      //[15:00-25:00)

      .count()
      .sort("window")
      .writeStream
      .outputMode(OutputMode.Complete())
      .option("truncate", value = false)
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
