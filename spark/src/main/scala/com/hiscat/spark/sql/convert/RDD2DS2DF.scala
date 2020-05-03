package com.hiscat.spark.sql.convert

import org.apache.spark.sql.SparkSession

object RDD2DS2DF {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("RDD2DS2DF")
      .getOrCreate()

    import spark.implicits._

    val rdd = spark.sparkContext.makeRDD(List(Person("hello", 18), Person("world", 19)))
    rdd.toDF("name", "age").show()
    rdd.toDS().show()

    spark.stop()
  }

  case class Person(name: String, age: Int)

}
