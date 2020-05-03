package com.hiscat.spark.streaming

import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._

import scala.collection.mutable

object UpdateStateByKey {


  def main(args: Array[String]): Unit = {
    val sc = new StreamingContext(
      new SparkConf().setAppName("UpdateStateByKey").setMaster("local[*]"),
      Seconds(3)
    )

    sc.checkpoint("output/checkpoint")
    sc.textFileStream("input/wordcount")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .updateStateByKey[Int]((vals: Seq[Int], count: Option[Int]) => Some(count.getOrElse(0) + vals.size))
      .print()

    sc.start()
    sc.awaitTermination()
  }
}
