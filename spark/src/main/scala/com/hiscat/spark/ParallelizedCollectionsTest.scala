package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object ParallelizedCollectionsTest {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("ParallelizedCollectionsTest")
        .setMaster("local[*]")
    )
    val data = Array(1 to 5: _*)
    println(
      sc
        .parallelize(data)
        .sum()
    )


    sc.stop()
  }
}
