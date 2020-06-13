package com.hiscat.online.edu.warehouse.app

import com.hiscat.online.edu.warehouse.bean.Member
import org.apache.spark.sql.{SaveMode, SparkSession}

object MemberEtl {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("MemberEtl")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._

    spark.read
      .json("hdfs://nameservice1/log/user/member.log")
      .as[Member]
      .map(m => {
        m.fullname = m.fullname.substring(0, 1) + "*" * (m.fullname.length - 1)
        m.password = "********"
        m.phone = m.phone.substring(0, 3) + "*" * 4 + m.phone.substring(8, m.phone.length)
        m
      })
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("dt", "dn")
      .saveAsTable("dwd.dwd_member")

    spark.stop()

  }
}
