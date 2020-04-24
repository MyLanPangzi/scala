package com.hiscat.spark.sql

import org.apache.spark.sql.SparkSession


object GettingStarted {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    val df = spark.read.json("input/people.json")
    df.show()
    import spark.implicits._
    df.printSchema()
    df.select("name").show()
    df.select($"name", $"age" + 1).show()
    df.filter($"age" > 21).show()
    df.groupBy($"age").count().show()
    df.createOrReplaceTempView("people")
    spark.sql("SELECT * FROM people").show()
    df.createGlobalTempView("people")
    spark.sql("SELECT * FROM people").show()
    spark.newSession().sql("SELECT * FROM global_temp.people").show()

    Seq(Person("Hello", 18)).toDS().show()
    Seq(1, 2, 3).toDS().map(_ + 1).show()
    spark.read.json("input/people.json").as[Person].show()

    val peopleDF = spark.sparkContext
      .textFile("input/people.txt")
      .map(_.split(","))
      .map(attrs => Person(attrs(0), attrs(1).trim.toInt))
      .toDF()
    peopleDF.createOrReplaceTempView("people")
    val teenagerDF = spark
      .sql("SELECT name,age FROM people WHERE age BETWEEN 13 AND 19")
    teenagerDF
      .map(r => s"NAME: ${r(0)}")
      .show()
    teenagerDF
      .map(teenager => s"NAME: ${teenager.getAs[String]("name")}")
      .show()

    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
    println(teenagerDF
      .map(teenager => teenager.getValuesMap[Any](List("name", "age")))
      .collect()
      .mkString(","))


  }

  case class Person(name: String, age: Long)

}
