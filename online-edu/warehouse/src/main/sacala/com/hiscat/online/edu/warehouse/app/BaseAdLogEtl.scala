package com.hiscat.online.edu.warehouse.app

import org.apache.spark.sql.{SaveMode, SparkSession}

object BaseAdLogEtl {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("BaseAdLogEtl")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.read
      .json("hdfs://nameservice1/log/user/baseadlog.log")
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("dn")
      .saveAsTable("dwd.dwd_base_ad")

    spark.stop()

  }
}
