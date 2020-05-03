package com.hiscat.spark.sql

import org.apache.spark.sql.SparkSession

object UDF {

  case class Employee(var name: String, var salary: Long)


  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("TypeSafeUDAF")
      .getOrCreate()

    import spark.implicits._
    val ds = spark.read.json("input/employees.json").as[Employee]
    spark.udf.register("toUpper", (x: String) => x.toUpperCase())
    ds.createOrReplaceTempView("emp")
    spark.sql("select toUpper(name) from emp").show()

    spark.stop()
  }
}
