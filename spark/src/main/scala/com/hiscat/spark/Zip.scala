package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object Zip {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("Zip")
        .setMaster("local[*]")
    )

    //    Can only zip RDDs with same number of elements in each partition
    //    sc.makeRDD(List(1,2,3))
    //        .zip(sc.makeRDD(List(1)))
    //        .collect()

    //Can't zip RDDs with unequal numbers of partitions: List(1, 2)
    //    sc.makeRDD(List(1, 2, 3), 1)
    //      .zip(sc.makeRDD(List(1), 2))
    //      .collect()
    println(
      sc.makeRDD(List(1, 2))
        .zip(sc.makeRDD(List("1", "2")))
        .collect()
        .mkString(",")
    )


    sc.stop()

  }
}
