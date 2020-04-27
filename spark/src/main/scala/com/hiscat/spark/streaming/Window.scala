package com.hiscat.spark.streaming

import java.time.{LocalDateTime, ZoneId}
import java.util.Date

import org.apache.spark._
import org.apache.spark.streaming._

object Window {


  def main(args: Array[String]): Unit = {
    val sc = new StreamingContext(
      new SparkConf().setAppName("Window").setMaster("local[*]"),
      Seconds(1)
    )

    sc.checkpoint("output/checkpoint")
    sc.textFileStream("input/wordcount")
      .flatMap(_.split(" "))
      .countByValueAndWindow(Durations.seconds(5), Durations.seconds(5))
      //      .map((_, 1))
      //      .window(Durations.seconds(10)).reduceByKey(_+_)
      //      .countByWindow(Durations.seconds(10), Durations.seconds(10))
      //      .reduceByWindow((w1, w2) => ("c", w1._2 + w2._2), Durations.seconds(10), Durations.seconds(10))
      //      .reduceByKeyAndWindow((v1: Int, v2: Int) => v1 + v2, Durations.seconds(10))
      .foreachRDD((rdd, time) => {
        println(LocalDateTime.ofInstant(new Date(time.milliseconds).toInstant, ZoneId.systemDefault()))
        println(rdd.collect().mkString(","))
      })

    sc.start()
    sc.awaitTermination()
  }
}
