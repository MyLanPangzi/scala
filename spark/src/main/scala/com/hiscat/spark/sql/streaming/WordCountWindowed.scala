package com.hiscat.spark.sql.streaming

import java.sql.Timestamp

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode

object WordCountWindowed {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("WordCountWindowed")
      .getOrCreate()
    import spark.implicits._

    //source
    spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", "9999")
      .option("includeTimestamp", value = true)
      .load()

      //transformation
      .as[(String, Timestamp)]
      .flatMap(line => line._1.split(" ").map((_, line._2)))
      .toDF("word", "timestamp")

      //grouping
      .groupBy(window($"timestamp", "10 seconds", "5 seconds"), $"word")
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
