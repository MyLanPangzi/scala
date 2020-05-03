package com.hiscat.spark.sql.streaming

import java.sql.Timestamp

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object StreamJoinStatic {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("StreamJoinStatic")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.readStream
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
      .join(Seq(("zs", 19), ("ls", 20), ("ww", 21)).toDF("name", "age"), Seq("name"), "left")

      .writeStream
      .outputMode(OutputMode.Update())
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
