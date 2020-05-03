package com.hiscat.spark.sql.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object FileSourcePartitionWordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("FileSourceWordCount")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val schema = StructType(
      StructField("name", StringType)
        :: StructField("age", IntegerType)
        :: StructField("year", StringType) :: Nil)
    spark.readStream
      .option("spark.sql.streaming.schemaInference", value = true)
      .schema(schema)
      .json("output")
      .writeStream
      .outputMode(OutputMode.Update())
      .format("console")
      .start()
      .awaitTermination()

    spark.stop()
  }
}
