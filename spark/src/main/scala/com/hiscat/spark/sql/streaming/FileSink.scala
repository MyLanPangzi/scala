package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object FileSink {
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
      .map(word => (word, word.reverse))
      .toDF("raw", "reverse")

      .writeStream
      .outputMode(OutputMode.Append())
      .option("path","output/json")
      .option("checkpointLocation","output/filesink")
      .format("json")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
