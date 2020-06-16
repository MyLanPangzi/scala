package com.hiscat.online.edu.warehouse.controller

import com.hiscat.online.edu.warehouse.service.{DwsMemberService, EtlService}
import com.hiscat.online.edu.warehouse.util.SparkUtil
import org.apache.spark.sql.SparkSession

object DwsMemberController extends App {
  val spark: SparkSession = SparkUtil.getHiveSparkSession("DwsMemberController")

  DwsMemberService.importMember(spark, "20190722")

  spark.stop()
}
