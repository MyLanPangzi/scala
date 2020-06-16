package com.hiscat.online.edu.warehouse.util

import org.apache.spark.sql.SparkSession

object SparkUtil {
  def getHiveSparkSession(appName: String, local: Boolean = true): SparkSession = {
//    System.setProperty("HADOOP_USER_NAME", "root")
    val builder = SparkSession.builder()
    if (local) {
      builder.master("local[*]")
    }
    val spark = builder
      .appName(appName)
      .enableHiveSupport()
      .getOrCreate()
    //ssc.hadoopConfiguration.set("fs.defaultFS", "hdfs://nameservice1")
    //ssc.hadoopConfiguration.set("dfs.nameservices", "nameservice1")

    HiveUtil.openCompression(spark)
    HiveUtil.openDynamicPartition(spark)
    spark
  }
}
