package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType

object StreamExample {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("StreamExample")
      .getOrCreate()
    import spark.implicits._

    val socketDF = spark.readStream
      .format("socket")
      .option("host", "hadoop102")
      .option("port", "9999")
      .load()
    println(socketDF.isStreaming)
    socketDF.printSchema()

    val schema = new StructType().add("name", "string").add("age", "integer")
    val people = spark.readStream
      .option("sep", ";")
      .option("header", "true")
      .schema(schema)
      .csv("input")

    people.printSchema()

    spark.stop()
  }
}
