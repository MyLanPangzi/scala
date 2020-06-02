package com.hiscat.flink.sink

import java.sql.PreparedStatement

import com.hiscat.flink.sensor.SensorReading
import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcExecutionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.scala._

object JdbcTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
env.setParallelism(1)
    env
      .addSource(new SensorSource)
      .addSink(JdbcSink.sink(
        "insert into sensor (id, timestamp, temperature) values (?,?,?)",
        new JdbcStatementBuilder[SensorReading] {
          override def accept(t: PreparedStatement, u: SensorReading): Unit = {
            t.setString(1, u.id)
            t.setLong(2, u.timestamp)
            t.setDouble(3, u.temperature)
          }
        },
        new JdbcExecutionOptions.Builder()
          .withBatchIntervalMs(1)
          .withBatchSize(10)
          .build(),
        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
          .withUrl("jdbc:mysql://hadoop102:3306/flink")
          .withDriverName("com.mysql.cj.jdbc.Driver")
          .withUsername("root")
          .withPassword("Xiebo0409")
          .build()));

    env.execute();
  }
}
