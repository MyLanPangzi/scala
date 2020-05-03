package com.hiscat.spark.sql

import org.apache.spark.sql.{Row, SaveMode, SparkSession}

object Hive1 {

  case class Record(key: Int, value: String)

  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Hive1")
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql

    sql("show databases").show()
    sql("use default").show()
    sql("show tables").show()
//    sql("create table world(id int, name string)").show()
//    sql("select * from hello").show()

    spark.stop()
  }
}
