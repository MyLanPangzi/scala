package com.hiscat.spark.sql.streaming

import java.sql.{Connection, DriverManager}
import java.util.{Timer, TimerTask}

import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.streaming.OutputMode

object ForeachSink {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("ForeachSink")
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
      .foreach(new ForeachWriter[Row] {
        var conn: Connection = _
        val sql = "replace into word_count values(?,?)"

        override def open(partitionId: Long, epochId: Long): Boolean = {
          Class.forName("com.mysql.cj.jdbc.Driver")
          conn = DriverManager.getConnection("jdbc:mysql://hadoop102:3306/spark", "root", "Xiebo0409")
          conn != null && !conn.isClosed
        }

        override def process(value: Row): Unit = {
          val preparedStatement = conn.prepareStatement(sql)
          preparedStatement.setString(1, value.getString(0))
          preparedStatement.setLong(2, value.getLong(1))
          preparedStatement.execute()
          preparedStatement.close()
        }

        override def close(errorOrNull: Throwable): Unit = {
          conn.close()
        }
      })
      .start()
      .awaitTermination()
    spark.stop()
  }
}
