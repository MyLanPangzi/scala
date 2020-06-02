package com.hiscat.flink.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.hiscat.flink.sensor.SensorReading
import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.configuration.Configuration
import org.apache.flink.connector.jdbc.{JdbcConnectionOptions, JdbcExecutionOptions, JdbcSink, JdbcStatementBuilder}
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._

object Jdbc2Test {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env
      .addSource(new SensorSource)
      .addSink(new RichSinkFunction[SensorReading] {

        var preparedStatement: PreparedStatement = _
        var connection: Connection = _

        override def open(parameters: Configuration): Unit = {
          connection = DriverManager.getConnection(
            "jdbc:mysql://hadoop102:3306/flink",
            "root",
            "Xiebo0409"
          )
          preparedStatement = connection.prepareStatement(
            " insert into sensor (id, timestamp, temperature) values (?,?,?) on duplicate key update timestamp = ?, temperature=? "
          )
        }

        override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
          preparedStatement.setString(1, value.id)
          preparedStatement.setLong(2, value.timestamp)
          preparedStatement.setDouble(3, value.temperature)
          preparedStatement.setLong(4, value.timestamp)
          preparedStatement.setDouble(5, value.temperature)
          preparedStatement.execute()

        }

        override def close(): Unit = {
          preparedStatement.close()
          connection.close()
        }
      })

    env.execute();
  }
}
