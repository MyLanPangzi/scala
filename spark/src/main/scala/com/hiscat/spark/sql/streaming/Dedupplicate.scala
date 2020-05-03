package com.hiscat.spark.sql.streaming

import java.sql.Timestamp

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

object Dedupplicate {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Dedupplicate")
      .master("local[*]")
      .getOrCreate()

    import org.apache.spark.sql.functions._
    import spark.implicits._

    //noinspection DuplicatedCode
    spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()


      .as[String]
      .map(e => {
        val s = e.split(",")
        (s(0), s(2), Timestamp.valueOf(s(1)))
      })
      .toDF("uid", "word", "ts")
      .dropDuplicates("uid")
//      .withWatermark("ts", "2 minutes")

      .writeStream
      .outputMode(OutputMode.Append())
      .option("truncate", value = false)
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
