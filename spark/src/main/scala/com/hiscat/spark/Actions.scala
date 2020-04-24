package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object Actions {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("apache.log")
        .setMaster("local[*]")
    )

    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .map(_ * 2)
        .reduce(_ + _)
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .map(_ * 2)
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .map(_ * 2)
        .count()
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .first()
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .take(2)
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .takeSample(withReplacement = false, 3)
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .takeSample(withReplacement = true, 2)
        .mkString(",")
    )
    println(
      sc.makeRDD(List(4, 3, 2, 1))
        .takeOrdered(2)
        .mkString(",")
    )
    //    sc.makeRDD(List(4, 3, 2, 1)).saveAsTextFile("output/nums")
    //    sc.makeRDD(List(4, 3, 2, 1)).saveAsObjectFile("output/objs")
    //    sc.makeRDD(List((1, 2), (3, 4))).saveAsSequenceFile("output/seqFile")
    println(
      sc.makeRDD(List((1, 2), (3, 4), (1, 3), (3, 3)))
        .countByKey()
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4, 1, 2, 3))
        .countByValue()
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4), 2)
        .aggregate(1)(_ + _, _ + _)
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4), 2)
        .fold(1)(_ + _)
    )
    sc.stop()
  }
}
