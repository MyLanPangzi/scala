package com.hiscat.spark.sql

import org.apache.spark.sql.SparkSession

object Json {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Json")
      .getOrCreate()
    import spark.implicits._

    val people = spark.read
      .json("input/people.json")
    people.printSchema()
    people.createOrReplaceTempView("people")
    spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19").show()

    val ds = spark.createDataset(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    spark.read.json(ds).show()

    spark.stop()
  }
}
