package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object Closure {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("ExternalDatasets")
        .setMaster("local[*]")
    )
    var count = 0
    val rdd = sc
      .parallelize(List(1, 2, 3, 4))

    rdd.foreach(x => count += x)
    println(count)
    sc.stop()
  }
}
