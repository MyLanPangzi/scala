package com.hiscat.flink.sql

import java.sql.Timestamp
import java.time.LocalDateTime

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.util.Collector

object Hop {
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
        |select id, count(*), hop_rowtime(ts, interval '2' second, interval '10' second)
        |from sensor
        |group by  hop(ts, interval '2' second, interval '10' second), id
        |""".stripMargin)
      .toAppendStream[(String, Long, Timestamp)]
      .process(new ProcessFunction[(String, Long, Timestamp), (String, Long, LocalDateTime)] {
        override def processElement(value: (String, Long, Timestamp),
                                    ctx: ProcessFunction[(String, Long, Timestamp), (String, Long, LocalDateTime)]#Context,
                                    out: Collector[(String, Long, LocalDateTime)]): Unit = {
          out.collect((value._1, value._2, value._3.toLocalDateTime))
        }
      })
      .print()


    tableEnvironment.execute("Hop")
  }
}
