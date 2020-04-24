package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object Accumulators {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("apache.log")
        .setMaster("local[*]")
    )
    val counter = sc.longAccumulator("counter")
    sc.parallelize(Array(1, 2, 3, 4)).foreach(counter.add(_))
    println(counter.value)
    sc.stop()
  }
}
