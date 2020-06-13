package com.hiscat.online.edu.warehouse.app

import org.apache.spark.sql.{SaveMode, SparkSession}

object PcenterMemPayMoneyEtl {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("PcenterMemPayMoneyEtl")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.read
      .json("hdfs://nameservice1/log/user/pcentermempaymoney.log")
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("dt", "dn")
      .saveAsTable("dwd.dwd_pcentermempaymoney")

    spark.stop()

  }
}
