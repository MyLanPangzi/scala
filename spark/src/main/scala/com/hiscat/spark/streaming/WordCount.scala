package com.hiscat.spark.streaming

import org.apache.spark._
import org.apache.spark.streaming._

object WordCount {
  def main(args: Array[String]): Unit = {
    val sc = new StreamingContext(
      new SparkConf().setAppName("WordCount").setMaster("local[*]"),
      Seconds(1)
    )

    //source
    sc.socketTextStream("localhost", 9999)
      //transformation
      .flatMap(_.split(" "))
      .map((_, 1))
      //grouping
      .reduceByKey(_ + _)
      //sink
      .print()

    sc.start()
    sc.awaitTermination()
  }
}
