package com.hiscat.online.edu.warehouse.app

import org.apache.spark.sql.{SaveMode, SparkSession}

object BaseWebsiteEtl {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("BaseWebsiteEtl")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.read
      .json("hdfs://nameservice1/log/user/baswewebsite.log")
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("dn")
      .saveAsTable("dwd.dwd_base_website")

    spark.stop()

  }
}
