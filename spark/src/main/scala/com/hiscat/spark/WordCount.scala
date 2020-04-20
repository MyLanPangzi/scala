package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}


object WordCount {
  def main(args: Array[String]): Unit = {
    new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("WordCount")
    )
      .textFile("input/word.txt")
      .flatMap(_.split(" "))
      .countByValue()
      .foreach(println)
  }
}
