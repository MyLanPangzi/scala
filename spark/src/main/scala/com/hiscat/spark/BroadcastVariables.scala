package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object BroadcastVariables {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("apache.log")
        .setMaster("local[*]")
    )

    val v = sc.broadcast(Array(1, 2, 3, 4))
    println(v.value.mkString(","))

    sc.stop()
  }
}
