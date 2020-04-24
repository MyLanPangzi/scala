package com.hiscat.spark

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object PartitionBy {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("PartitionBy")
        .setMaster("local[*]")
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8))
        .mapPartitionsWithIndex((i, it) => it.map((i, _)))
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List((1,2)))
        .partitionBy(new HashPartitioner(3))

    )
    sc.stop()

  }
}
