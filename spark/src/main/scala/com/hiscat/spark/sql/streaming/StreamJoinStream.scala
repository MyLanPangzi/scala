package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object StreamJoinStream {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("StreamJoinStream")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val left = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

      .as[String]
      .map(e => {
        val s = e.split(",")
        (s(0), s(1))
      })
      .toDF("name", "sex")

    val right = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 10000)
      .load()

      .as[String]
      .map(e => {
        val s = e.split(",")
        (s(0), s(1))
      })
      .toDF("name", "age")

    left.join(right, "name")
      .writeStream
      .outputMode(OutputMode.Append())
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
