package com.hiscat.spark.sql.streaming

import java.util.{Timer, TimerTask}
import java.util.concurrent.{Executors, TimeUnit}

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode

object MemorySink {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("MemorySink")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val query = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

      .as[String]
      .flatMap(_.split(" "))
      .toDF("value")

      .writeStream
      .outputMode(OutputMode.Append())
      .option("checkpointLocation", "output/memorysink4")
      .format("memory")
      .queryName("word")
      .start()

    val timer = new Timer
    timer.scheduleAtFixedRate(new TimerTask {
      override def run(): Unit = {
        spark.sql("select count(*),value word from word group by value").show()
      }
    }, 2000,2000)
    query.awaitTermination()
    spark.stop()
  }
}
