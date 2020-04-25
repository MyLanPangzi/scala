package com.hiscat.spark.sql

import java.util.Properties

import org.apache.spark.sql.{Row, SaveMode, SparkSession}

object Jdbc {

  case class Record(key: Int, value: String)


  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Jdbc")
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql

    val users = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/gmall")
      .option("dbtable", "gmall.user_info")
      .option("user", "root")
      .option("password", "Xiebo0409")
      .load()
    users.show(3)

    val props: Properties = new Properties()
    props.put("user", "root")
    props.put("password", "Xiebo0409")
    spark.read
      .jdbc("jdbc:mysql://hadoop102:3306/gmall", "user_info", props)
      .show(3)

    props.put("customSchema", "id int, name STRING")
    val df = spark.read
      .jdbc("jdbc:mysql://hadoop102:3306/gmall", "user_info", props)
    //    df.show(3)
    df.limit(3)
      .map(row => (row.getInt(0), row.getString(4)))
      .foreach(println(_))

    df.write
      .mode(SaveMode.Overwrite)
      .option("createTableOptions", "char set utf8")
      .jdbc("jdbc:mysql://hadoop102:3306/gmall?useUnicode=true&characterEncoding=utf8", "user_id_name", props)

    users
      .map(row => (row.getLong(0), row.getString(4)))
      .toDF("bigid", "name")
      .write
      .mode(SaveMode.Overwrite)
      .option("createTableOptions", "char set utf8")
      .option("createTableColumnTypes", "bigid bigint, name VARCHAR(1024)")
      .jdbc("jdbc:mysql://hadoop102:3306/gmall?useUnicode=true&characterEncoding=utf8", "user_id_name_2", props)

    spark.stop()
  }
}
