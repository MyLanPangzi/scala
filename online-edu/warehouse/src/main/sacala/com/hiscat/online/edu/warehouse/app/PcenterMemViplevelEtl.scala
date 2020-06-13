package com.hiscat.online.edu.warehouse.app

import com.hiscat.online.edu.warehouse.bean.Member
import org.apache.spark.sql.{SaveMode, SparkSession}

object PcenterMemViplevelEtl {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("PcenterMemViplevelEtl")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._

    spark.read
      .json("hdfs://nameservice1/log/user/pcenterMemViplevel.log")
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("dn")
      .saveAsTable("dwd.dwd_vip_level")

    spark.stop()

  }
}
