package com.hiscat.flink.table

import java.sql.Timestamp

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.Tumble
import org.apache.flink.table.api.scala._

object RetractTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    tableEnvironment.fromDataStream(
      env.addSource(new SensorSource).assignAscendingTimestamps(_.timestamp),
      'id, 'ts.rowtime
    )
      .window(Tumble over 10.seconds on 'ts as 'w)
      .groupBy('id, 'w)
      .select('id, 'id.count, 'w.rowtime, 'w.start, 'w.end)
      .toRetractStream[(String, Long, Timestamp, Timestamp, Timestamp)]
      .print()

    tableEnvironment.execute("retract")
  }
}
