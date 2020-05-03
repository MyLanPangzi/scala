package com.hiscat.spark.streaming

import java.time.{Instant, ZoneOffset}

import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SqlWordCount {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(
      new SparkConf().setAppName("SqlWordCount").setMaster("local[*]"),
      Seconds(3)
    )

    ssc.socketTextStream("localhost", 9999)
      .flatMap(_.split(" "))
      .foreachRDD((rdd, t) => {
        val spark = SparkSessionSingleton.getInstance(rdd.sparkContext.getConf)
        import spark.implicits._

        println(Instant.ofEpochMilli(t.milliseconds).atOffset(ZoneOffset.ofHours(8)))
        rdd.map(Record).toDF().createOrReplaceTempView("words")
        spark.sql("select word,count(*) total from words group by word")
          .show()
      })

    ssc.start()
    ssc.awaitTermination()
  }

  case class Record(word: String)

  object SparkSessionSingleton {
    @transient private var instance: SparkSession = _

    def getInstance(sparkConf: SparkConf): SparkSession = {
      if (instance == null) {
        instance = SparkSession.builder().config(sparkConf).getOrCreate()
      }
      instance
    }
  }

}
