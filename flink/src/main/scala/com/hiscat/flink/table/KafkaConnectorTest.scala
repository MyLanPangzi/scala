package com.hiscat.flink.table

import java.sql.Date
import java.time.LocalDate
import java.util

import com.hiscat.flink.table.JdbcConnectorTest.User
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.KafkaTableSourceSinkFactory
import org.apache.flink.table.api.{DataTypes, EnvironmentSettings}
import org.apache.flink.table.api.scala._
import org.apache.flink.table.descriptors.{Csv, Json, Kafka, Schema}
import org.apache.flink.types.Row

object KafkaConnectorTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val settings = EnvironmentSettings
      .newInstance()
      .useBlinkPlanner()
      .build()
    val tableEnvironment = StreamTableEnvironment.create(env, settings)

    tableEnvironment
      .connect(
        new Kafka()
          .version("universal")
          //   "0.8", "0.9", "0.10", "0.11", and "universal"
          .topic("user") // required: topic name from which the table is read

          // optional: connector specific properties
          .property("zookeeper.connect", "hadoop102:2181")
          .property("bootstrap.servers", "hadoop102:9092")
          .property("group.id", "testGroup")

          // optional: select a startup mode for Kafka offsets
          .startFromEarliest()
          //          .startFromLatest()
          //        .startFromSpecificOffsets(...)

          // optional: output partitioning from Flink's partitions into Kafka's partitions
          //   .sinkPartitionerFixed() // each Flink partition ends up in at-most one Kafka partition (default)
          .sinkPartitionerRoundRobin() // a Flink partition is distributed to Kafka partitions round-robin
        //      .sinkPartitionerCustom(MyCustom.class)    // use a custom FlinkKafkaPartitioner subclass
      )
      .withSchema(
        new Schema()
          .field("id", "bigint")
          .field("name", "string")
          .field("birthday", "date")
      )
      .withFormat(new Json)
      .inAppendMode()
      .createTemporaryTable("user")

    tableEnvironment
      .sqlQuery("select * from `user`")
      .toAppendStream[(Long, String, Date)]
      .print()
    val stream = env.fromElements(
      User(1, "1", Date.valueOf(LocalDate.now()))
    )
    tableEnvironment
      .fromDataStream(stream)
      .insertInto("user")

    tableEnvironment.execute("kafka connector")
  }
}
