package com.hiscat.spark

import java.text.SimpleDateFormat

import org.apache.spark.{SparkConf, SparkContext}

object GroupBy {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("WordCount")
    )

    println(
      sc.makeRDD(List("Hello", "Hadoop", "Hive", "Scala", "Spark"))
        .groupBy(_ (0).toUpper)
        .collect()
        .mkString(",")
    )
    println(
      sc.textFile("input/apache.log")
        .map(_.split(" ")(3))
        .map(_.substring(11, 13))
        .groupBy(s => s)
        .mapValues(_.size)
        .collect()
        .mkString(",")
    )
    sc.stop()
  }
}
