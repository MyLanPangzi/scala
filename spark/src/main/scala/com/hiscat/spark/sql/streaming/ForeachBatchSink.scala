package com.hiscat.spark.sql.streaming

import java.sql.{Connection, DriverManager}
import java.util.Properties

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{ForeachWriter, Row, SaveMode, SparkSession}

object ForeachBatchSink {


  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("ForeachBatchSink")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

      .as[String]
      .flatMap(_.split(" "))
      .toDF("value")
      .groupBy("value")
      .count()

      .writeStream
      .outputMode(OutputMode.Update())
      .foreachBatch((rows, _) => {
        val props: Properties = new Properties()
        props.setProperty("user", "root")
        props.setProperty("password", "Xiebo0409")
        rows.persist()
        rows.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://hadoop102:3306/spark", "word_count", props)
        rows.write.mode(SaveMode.Overwrite).json("output/foreachbatch")
        rows.unpersist()
      })
      .start()
      .awaitTermination()
    spark.stop()
  }
}
