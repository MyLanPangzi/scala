package com.hiscat.spark.sql

import com.hiscat.spark.sql.GettingStarted.Person
import org.apache.spark.sql.{Row, SparkSession, types}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object InteroperatingWithRDDs {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("InteroperatingWithRDDs")
      .getOrCreate()
    import spark.implicits._

    val rows = spark.sparkContext
      .textFile("input/people.txt")
      .map(_.split(","))
      .map(attrs => Row(attrs(0), attrs(1).trim))

    val schema = types.StructType(
      "name age"
        .split(" ")
        .map(f => StructField(f, StringType, nullable = true))
    )
    val df = spark.createDataFrame(rows, schema)
    df.createOrReplaceTempView("people")

    spark.sql("SELECT name FROM people").show()

    spark.stop()
  }

  case class Person(name: String, age: Long)

}
