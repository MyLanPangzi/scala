package com.hiscat.spark.sql.convert

import org.apache.spark.sql.{Row, SparkSession}

object DF2DS2RDD {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("DF2DS2RDD")
      .getOrCreate()

    import spark.implicits._

    val df = Seq(Person("hello", 18), Person("world", 19))
      .toDF("name", "age")
    df.show()
    df.rdd.collect().foreach {
      case Row(name, age) => println(s"$name, $age")
    }
    df.as[Person].show()

    spark.stop()
  }

  case class Person(name: String, age: Int)

}
