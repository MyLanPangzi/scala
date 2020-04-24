package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object ApacheLogTest {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("apache.log")
        .setMaster("local[*]")
    )
    println(sc
      .makeRDD(List(1, 2, 3, 4), 2)
      .mapPartitions(_.map(_ * 2))
      .collect()
      .mkString(","))
    println(sc
      .makeRDD(List(1, 2, 3, 4), 2)
      .mapPartitionsWithIndex((i, v) => v.map(_ * (i + 1)))
      .collect()
      .mkString(","))
    println(sc
      .makeRDD(List(List(1, 2), List(3, 4)))
      .flatMap(e => e)
      .collect()
      .mkString(","))

    println(sc
      .makeRDD(List( 1, 2, 3, 4), 2)
      .mapPartitions(it => Iterable(it.max).iterator)
      .collect()
      .mkString(",")
    )


    //    println(sc
    //      .textFile("input/apache.log")
    //      .map(_.split(" "))
    //      .filter(_.length >= 6)
    //      .map(_ (6))
    //      .collect()
    //      .mkString(","))

    sc.stop()
  }
}
