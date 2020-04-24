package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object Print {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("ExternalDatasets")
        .setMaster("local[*]")
    )
    val rdd = sc
      .parallelize(List(1, 2, 3, 4))
    //避免使用
    rdd.foreach(println)
    rdd.take(10).foreach(println)
    sc.stop()

  }
}
