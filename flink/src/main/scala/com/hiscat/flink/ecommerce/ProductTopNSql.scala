package com.hiscat.flink.ecommerce

import java.sql.Timestamp

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.scala._

object ProductTopNSql {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env, EnvironmentSettings.newInstance().useBlinkPlanner().build())


    val stream = env
      .readTextFile("E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\UserBehavior.csv").name("user behavior source")
      .filter(_.isEmpty)
      .map(_.split(","))
      .filter(e => e(3) == "pv")
      .map(strings => {
        (strings(1).toInt, strings(4).toLong * 1000)
      })
    tableEnvironment.createTemporaryView("pv", stream, 'item, 'ts.rowtime)

    //    val table = tableEnvironment.fromDataStream(stream, 'item, 'ts.rowtime)

    tableEnvironment.sqlQuery(
      """
        |select t.item, t.num, wend
        |from (
        |         select t.item, num, wend, row_number() over (partition by wend order by num desc) pvrank
        |         from (
        |                  select item, count(*) num, tumble_end(ts, INTERVAL  '1'  hour) as wend
        |                  from pv
        |                  group by item, tumble(ts, INTERVAL '1' hour)
        |              ) t
        |     ) t
        |where t.pvrank <= 3
        |""".stripMargin)
      .toRetractStream[(Int, Long, Timestamp)]
      .print()

    env.execute()
  }


}
