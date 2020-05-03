package com.hiscat.spark.streaming

import java.time.{Instant, ZoneOffset}

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object RawNetworkGrep {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(
      new SparkConf().setMaster("local[*]").setAppName("RawNetworkGrep"),
      Seconds(5)
    )

    val rawStreams = (1 to 1).map(_ =>
      ssc.rawSocketStream[String]("localhost", 9999, StorageLevel.MEMORY_ONLY_SER))
      .toArray

    ssc.union(rawStreams)
      .filter(_.contains("the"))
      .count()
      .foreachRDD((rdd, t) => {
        println(Instant.ofEpochMilli(t.milliseconds).atOffset(ZoneOffset.ofHours(8)))
        println(s"Grep count: ${rdd.collect.mkString}")
      })

    ssc.start()
    ssc.awaitTermination()
  }
}
