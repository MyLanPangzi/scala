package com.hiscat.spark.structured.streaming

import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object NetworkWordCount {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("NetworkWordCount")
      .getOrCreate()
    import spark.implicits._

    spark.readStream
      .format("socket")
      .option("host", "hadoop102")
      .option("port", 9999)
      .load()
      .as[String]
      .flatMap(_.split(" "))
      .groupBy("value")
      .count()
      .writeStream.outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
