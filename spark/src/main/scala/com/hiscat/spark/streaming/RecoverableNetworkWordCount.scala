package com.hiscat.spark.streaming

import java.io.File
import java.nio.charset.Charset
import java.time.{Instant, ZoneOffset}

import com.google.common.io.Files
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object RecoverableNetworkWordCount {

  def createContext(): StreamingContext = {
    val outputFile = new File("output/test")
    if (outputFile.exists()) outputFile.delete()
    val ssc = new StreamingContext(
      new SparkConf().setAppName("RecoverableNetworkWordCount").setMaster("local[*]"),
      Seconds(3)
    )
    ssc.checkpoint("input/checkpoint")
    ssc.socketTextStream("localhost", 9999)
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .foreachRDD((rdd, t) => {
        val blackList = WordBlacklist.getInstance(rdd.sparkContext)
        val droppedWordCounter = DroppedWordsCounter.getInstance(rdd.sparkContext)
        val counts = rdd.filter {
          case (word, count) => if (blackList.value.contains(word)) {
            droppedWordCounter.add(count)
            false
          } else {
            true
          }
        }.collect().mkString("[", ",", "]")
        val time = Instant.ofEpochMilli(t.milliseconds).atOffset(ZoneOffset.ofHours(8))
        val output = s"Counts at time $time $counts"
        println(output)
        println(s"Dropped ${droppedWordCounter.value} word(s) totally")
        println(s"Appending to ${outputFile.getAbsolutePath}")
        //noinspection UnstableApiUsage
        Files.append(output + "\n", outputFile, Charset.defaultCharset())
      })

    ssc
  }

  def main(args: Array[String]): Unit = {
    val ssc = StreamingContext.getOrCreate("input/checkpoint", createContext)
    ssc.start()
    ssc.awaitTermination()
  }

  object WordBlacklist {

    @volatile private var instance: Broadcast[Seq[String]] = null

    def getInstance(sc: SparkContext): Broadcast[Seq[String]] = {
      if (instance == null) {
        synchronized {
          if (instance == null) {
            val wordBlacklist = Seq("a", "b", "c")
            instance = sc.broadcast(wordBlacklist)
          }
        }
      }
      instance
    }
  }

  object DroppedWordsCounter {

    @volatile private var instance: LongAccumulator = _

    def getInstance(sc: SparkContext): LongAccumulator = {
      if (instance == null) {
        synchronized {
          if (instance == null) {
            instance = sc.longAccumulator("WordsInBlacklistCounter")
          }
        }
      }
      instance
    }
  }

}
