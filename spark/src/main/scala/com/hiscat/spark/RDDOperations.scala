package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object RDDOperations {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("ExternalDatasets")
        .setMaster("local[*]")
    )

    sc.stop()
  }
}
