package com.hiscat.online.edu.warehouse.controller

import com.hiscat.online.edu.warehouse.service.EtlService
import com.hiscat.online.edu.warehouse.util.SparkUtil
import org.apache.spark.sql.SparkSession

object DwdMemberController extends App {
  val spark: SparkSession = SparkUtil.getHiveSparkSession("DwdMemberController")

  EtlService.etlBaseAdLog(spark)
  EtlService.etlBaseWebsite(spark)
  EtlService.etlVipLevel(spark)
  EtlService.etlMemberRegtype(spark)
  EtlService.etlMember(spark)
  EtlService.etlPcenterMemPayMoney(spark)

  spark.stop()
}
