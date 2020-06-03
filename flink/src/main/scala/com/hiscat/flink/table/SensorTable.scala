package com.hiscat.flink.table

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._

object SensorTable {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    tableEnvironment.fromDataStream(env.addSource(new SensorSource))
      .select("id as d,timestamp,temperature")
      .toAppendStream[(String, Long, Double)]
      .print()

    tableEnvironment.execute("sensor")
  }
}
