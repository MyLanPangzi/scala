package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession

object FileSourceWordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("FileSourceWordCount")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.readStream
      .text("output")

      .as[String]
      .flatMap(_.split(" "))
      .groupBy("value")
      .count()

      .writeStream
      .outputMode("complete")
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
