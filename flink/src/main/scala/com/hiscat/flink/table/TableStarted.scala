package com.hiscat.flink.table

import java.util

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.{DataTypes, EnvironmentSettings}
import org.apache.flink.table.api.scala._
import org.apache.flink.table.descriptors.{ConnectorDescriptor, Csv, FileSystem, OldCsv, Schema}
import org.apache.flink.table.types.DataType

object TableStarted {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv = StreamTableEnvironment.create(env, EnvironmentSettings.newInstance().useBlinkPlanner().build())

    val input = env.fromElements(
      Person(1, "hello", 18),
      Person(1, "world", 19),
      Person(1, "flink", 20)
    )

    tableEnv
      .connect(new FileSystem().path("output/person"))
      .withFormat(new Csv)
      .withSchema(
        new Schema()
          .field("id", DataTypes.BIGINT())
          .field("name", DataTypes.STRING())
          .field("age", DataTypes.INT())
      )
      .createTemporaryTable("outtable")

    //    tableEnv.createTemporaryView("person",input)

    tableEnv.fromDataStream(input)
      .insertInto("outtable")

    tableEnv.execute("table started")
  }

  case class Person(id: Long, name: String, age: Int)

}
