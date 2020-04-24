package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object KV {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("KV")
        .setMaster("local[*]")
    )
    println(sc
      .textFile("input/word.txt")
      .flatMap(_.split(" "))
      .map(s => (s, 1))
      .reduceByKey(_ + _)
      .sortBy(_._2)
      .collect()
      .mkString(",")
    )
    sc.stop()

  }
}
