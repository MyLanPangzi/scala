package com.hiscat.online.edu.warehouse.app

import org.apache.spark.sql.{SaveMode, SparkSession}

object MemberRegtypeEtl {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("BaseWebsiteEtl")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.read
      .json("hdfs://nameservice1/log/user/memberRegtype.log")
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("dt", "dn")
      .saveAsTable("dwd.dwd_member_regtype")

    spark.stop()

  }
}
