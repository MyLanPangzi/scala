package com.hiscat.spark.sql

import java.io.File
import java.nio.file.{Files, Path, Paths}

import org.apache.spark.sql.SparkSession

object BasicDatasourceExample {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("BasicDatasourceExample")
      .getOrCreate()


    val users = spark.read.load("input/users.parquet")
    users.select("name", "favorite_color").write.save("output/nameAndFavorColors.parquet")

    val people = spark.read.format("json").load("input/people.json")
    people.select("name", "age").write.save("output/nameAndAges.parquet")

    spark.read
      .option("sep", ";")
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("input/people.csv")
      .show()

    users.write.format("orc")
      .option("orc.boolm.filter.columns", "favorite_color")
      .option("orc.dictionary.key.threshold", "1.0")
      .save("output/users_with_options.orc")
    spark.sql("SELECT * FROM parquet.`input/users.parquet`").show()

    users.write
      .option("path", "output/table")
      .saveAsTable("t")

    people.write
      .option("path", "output/people")
      .partitionBy("age")
      .bucketBy(4, "name")
      //      .sortBy("age")
      .saveAsTable("people")

    users.write
      .format("parquet")
      .partitionBy("favorite_color")
      .save("output/namesPartitionByColor.parquet")

    users.write
      .option("path", "output/users_partitioned_bucketed")
      .partitionBy("favorite_color")
      .bucketBy(42, "name")
      .saveAsTable("users_partitioned_bucketed")

    spark.stop()
  }
}
