package com.hiscat.flink.table

import java.sql.Date
import java.time.LocalDate

import com.typesafe.sslconfig.ssl.FakeChainedKeyStore.User
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.scala._

object JdbcConnectorTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, bsSettings)


    tableEnv.sqlUpdate(
      s"""
         |CREATE TABLE `user` (
         |    id bigint,
         |    name string,
         |    birthday date
         |) WITH (
         |  'connector.type' = 'jdbc', -- required: specify this table type is jdbc
         |
         |  'connector.url' = 'jdbc:mysql://hadoop102:3306/flink', -- required: JDBC DB url
         |
         |  'connector.table' = 'user',  -- required: jdbc table name
         |
         |  'connector.driver' = 'com.mysql.cj.jdbc.Driver', -- optional: the class name of the JDBC driver to use to connect to this URL.
         |                                                -- If not set, it will automatically be derived from the URL.
         |
         |  'connector.username' = 'root', -- optional: jdbc user name and password
         |  'connector.password' = 'Xiebo0409',
         |
         |  -- scan options, optional, used when reading from table
         |
         |  -- These options must all be specified if any of them is specified. In addition, partition.num must be specified. They
         |  -- describe how to partition the table when reading in parallel from multiple tasks. partition.column must be a numeric,
         |  -- date, or timestamp column from the table in question. Notice that lowerBound and upperBound are just used to decide
         |  -- the partition stride, not for filtering the rows in table. So all rows in the table will be partitioned and returned.
         |  -- This option applies only to reading.
         |  -- 'connector.read.partition.column' = 'column_name', -- optional, name of the column used for partitioning the input.
         |  -- 'connector.read.partition.num' = '50', -- optional, the number of partitions.
         |  -- 'connector.read.partition.lower-bound' = '500', -- optional, the smallest value of the first partition.
         |  -- 'connector.read.partition.upper-bound' = '1000', -- optional, the largest value of the last partition.
         |
         |  'connector.read.fetch-size' = '100', -- optional, Gives the reader a hint as to the number of rows that should be fetched
         |                                       -- from the database when reading per round trip. If the value specified is zero, then
         |                                       -- the hint is ignored. The default value is zero.
         |
         |  -- lookup options, optional, used in temporary join
         |  'connector.lookup.cache.max-rows' = '5000', -- optional, max number of rows of lookup cache, over this value, the oldest rows will
         |                                              -- be eliminated. "cache.max-rows" and "cache.ttl" options must all be specified if any
         |                                              -- of them is specified. Cache is not enabled as default.
         |  'connector.lookup.cache.ttl' = '10s', -- optional, the max time to live for each rows in lookup cache, over this time, the oldest rows
         |                                        -- will be expired. "cache.max-rows" and "cache.ttl" options must all be specified if any of
         |                                        -- them is specified. Cache is not enabled as default.
         |  'connector.lookup.max-retries' = '3', -- optional, max retry times if lookup database failed
         |
         |  -- sink options, optional, used when writing into table
         |  'connector.write.flush.max-rows' = '5000', -- optional, flush max size (includes all append, upsert and delete records),
         |                                             -- over this number of records, will flush data. The default value is "5000".
         |  'connector.write.flush.interval' = '2s', -- optional, flush interval mills, over this time, asynchronous threads will flush data.
         |                                           -- The default value is "0s", which means no asynchronous flush thread will be scheduled.
         |  'connector.write.max-retries' = '3' -- optional, max retry times if writing records to database failed
         |)
         |""".stripMargin)


    tableEnv.sqlQuery("select * from `user`")
      .select('id, 'name, 'birthday)
      .toAppendStream[(Long, String, Date)]
      .print()
    val stream = env.fromElements(
      User(2, "2", Date.valueOf(LocalDate.now()))
    )

    tableEnv.fromDataStream(stream)
      .insertInto("user")

    tableEnv.execute("mysql test")
  }

  case class User(id: Long, name: String, date: Date)

}
