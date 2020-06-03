package com.hiscat.flink.sql

import java.sql.Timestamp

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._

object Tumble {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val tableEnvironment = StreamTableEnvironment.create(env)

    tableEnvironment.createTemporaryView(
      "sensor",
      env.addSource(new SensorSource).assignAscendingTimestamps(_.timestamp),
      'id, 'ts.rowtime)
    tableEnvironment.sqlQuery(
      """
        |select id, count(*)
        |from sensor
        |group by  tumble(ts, interval '10' second), id
        |""".stripMargin)
      .toAppendStream[(String, Long)]
      .print()


    tableEnvironment.execute("tumble")
  }
}
