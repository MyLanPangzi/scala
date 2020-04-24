package com.hiscat.spark.sql

import org.apache.spark.sql.SparkSession

object Parquet {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Parquet")
      .getOrCreate()
    // Encoders for most common types are automatically provided by importing spark.implicits._
    import spark.implicits._
    val people = spark.read.json("input/people.json")
    people.write.save("output/people.parquet")

    val parquet = spark.read.parquet("output/people.parquet")
    parquet.createOrReplaceTempView("people")
    spark.sql("SELECT * FROM PEOPLE WHERE age BETWEEN 13 AND 19").show()

    // Create a simple DataFrame, store into a partition directory
    spark.sparkContext
      .makeRDD(1 to 5)
      .map(i => (i, i * i))
      .toDF("value", "square")
      .write
      .parquet("output/test_table/key=1")

    // Create another DataFrame in a new partition directory, adding a new column and dropping an existing column
    spark.sparkContext
      .makeRDD(6 to 10)
      .map(i => (i, i * i * i))
      .toDF("value", "cube")
      .write
      .parquet("output/test_table/key=2")

    spark.read
      .option("mergeSchema", "true")
      .parquet("output/test_table")
      .printSchema()


    spark.stop()
  }
}
