package com.hiscat.spark.streaming

import org.apache.spark._
import org.apache.spark.streaming._

object TextFileStream {
  def main(args: Array[String]): Unit = {
    val sc = new StreamingContext(
      new SparkConf().setAppName("TextFileStream").setMaster("local[*]"),
      Seconds(1)
    )

//    sc.textFileStream("hdfs://hadoop102:9000/wordcount")
    sc.textFileStream("input/wordcount")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()

    sc.start()
    sc.awaitTermination()
  }
}
