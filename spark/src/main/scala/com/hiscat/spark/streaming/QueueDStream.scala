package com.hiscat.spark.streaming

import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._

import scala.collection.mutable

object QueueDStream {


  def main(args: Array[String]): Unit = {
    val sc = new StreamingContext(
      new SparkConf().setAppName("QueueDStream").setMaster("local[*]"),
      Seconds(1)
    )
    val queue: mutable.Queue[RDD[String]] = mutable.Queue(sc.sparkContext.makeRDD(List("hello", "world", "hello", "scala")))

    sc.queueStream(queue)
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()

    sc.start()

    for (_ <- 1 to 30) {
      queue.synchronized {
        queue += sc.sparkContext.makeRDD(List("hello", "scala", "hello", "hadoop"))
      }
      Thread.sleep(1000)
    }
    sc.awaitTermination()
  }
}
