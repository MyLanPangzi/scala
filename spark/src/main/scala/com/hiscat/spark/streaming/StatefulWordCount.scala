package com.hiscat.spark.streaming

import java.time.{Instant, ZoneOffset}

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

object StatefulWordCount {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(
      new SparkConf().setAppName("StatefulNetworkWordCount").setMaster("local[*]"),
      Seconds(3)
    )
    ssc.checkpoint("input/stateful-wordcount")
    ssc.socketTextStream("localhost", 9999)
      .flatMap(_.split(" "))
      .map((_, 1))
      .mapWithState(
        StateSpec.function(
          (word: String, one: Option[Int], state: State[Int]) => {
            val sum = one.getOrElse(0) + state.getOption().getOrElse(0)
            state.update(sum)
            (word, sum)
          }
        )
      )
      .foreachRDD((rdd, t) => {
        println(Instant.ofEpochMilli(t.milliseconds).atOffset(ZoneOffset.ofHours(8)))
        println(rdd.collect().mkString)
      })

    ssc.start()
    ssc.awaitTermination()
  }
}
