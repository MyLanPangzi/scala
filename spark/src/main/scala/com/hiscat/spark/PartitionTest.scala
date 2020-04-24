package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object PartitionTest {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        //        .setMaster("local")
        //        .set("spark.default.parallelism", "4")
        .setAppName("partitions")
    )

    //    val rdd = sc.makeRDD(List(1, 2))
    //    val rdd = sc.makeRDD(List(1, 2, 3, 4, 5), 3)
    //    println(rdd.partitions.length)
    sc
      .textFile("input/test.txt", 2)
      .map(_.toInt)
      .saveAsTextFile("output/test")
    //    rdd.saveAsTextFile("output/partitions")

    sc.stop()
  }
}
