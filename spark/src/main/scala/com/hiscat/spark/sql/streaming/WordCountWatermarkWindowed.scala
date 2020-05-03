package com.hiscat.spark.sql.streaming

import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneOffset}

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode

object WordCountWatermarkWindowed {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("WordCountWatermarkWindowed")
      .getOrCreate()
    import spark.implicits._

    //source
    spark.readStream
      .format("socket")
      .option("host", "hadoop102")
      .option("port", "9999")
      .load()

      //transformation
      .as[String]
      .map(line => {
        val strings = line.split(" ")
        (strings(0),
          Timestamp.from(
            LocalDateTime.parse(strings(1), DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss"))
              .toInstant(ZoneOffset.ofHours(8))
          )
        )
      })
      .toDF("word", "timestamp")

      //grouping
      .withWatermark("timestamp", "10 minutes")
      .groupBy(window($"timestamp", "10 minutes"), $"word")
      .count()
      .orderBy("window")

      //sink
      .writeStream.outputMode(OutputMode.Complete())
      .option("truncate", "false")
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
