package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object RddFuncTest {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("rddFuncs")
        .setMaster("local[*]")
    )

    println(
      sc
        .makeRDD(List(1, 2, 3, 4), 2)
        .glom()
        .collect()
        .map(_.mkString(","))
        .mkString(","))
    println(
      sc
        .makeRDD(List(1, 2, 3, 4), 2)
        .glom()
        .map(_.max)
        .sum()
    )

    sc.stop()
  }
}
