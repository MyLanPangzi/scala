package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object Checkpoint {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("Checkpoint")
        .setMaster("local[*]")

    )
    sc.setCheckpointDir("input/checkpoint")
    val rdd = sc.makeRDD(List(1, 2, 3, 4)).map(i => {
      println("i")
      i * 2
    })
//    println(rdd.sum())
    println(rdd.toDebugString)
    val rdd1 = rdd.cache()
    println(rdd.toDebugString)
    rdd1.checkpoint()
    println(rdd1.toDebugString)
    println(rdd1.sum())
    println(rdd1.sum())

    sc.stop()
  }
}
