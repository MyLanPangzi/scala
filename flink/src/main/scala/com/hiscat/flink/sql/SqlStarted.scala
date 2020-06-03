package com.hiscat.flink.sql

import java.sql.Timestamp

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.Tumble
import org.apache.flink.table.api.scala._

object SqlStarted {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    tableEnvironment.sqlUpdate(
      """
        |create table person(name string, age int)
        |with(
        |'connector.type' = 'filesystem',
        |'connector.path' = 'E:/github/scala/input/people.txt',
        |'format.type' = 'csv'
        |)
        |""".stripMargin)
    tableEnvironment.sqlQuery("select * from person")
      .toAppendStream[(String, Int)]
      .print()

    tableEnvironment.execute("sql")
  }
}
