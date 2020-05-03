package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object RateSource {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("RateSource")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.readStream
      .format("rate")
      .option("rowsPerSecond", "10")
      .option("rampUpTime", "1")
      .option("numPartitions", "4")
      .load()

      .writeStream
      .outputMode(OutputMode.Update())
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
