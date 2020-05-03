package com.hiscat.spark.sql.convert

import org.apache.spark.sql.{Row, SparkSession}

object DS2DF2RDD {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("DF2DS2RDD")
      .getOrCreate()

    import spark.implicits._

    val ds = Seq(Person("hello", 18), Person("world", 19)).toDS()
    ds.toDF().show()
    ds.rdd.collect().foreach {
      case Person(name, age) => println(s"$name,$age")
    }

    spark.stop()
  }

  case class Person(name: String, age: Int)

}
