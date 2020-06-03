package com.hiscat.flink.sql.function

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.functions.TableFunction

object Table {


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    val split = new Split()
    tableEnvironment.fromDataStream(env.fromElements("hello world"), 'sentence)
//      .joinLateral(split('sentence) as('word, 'length))
      .leftOuterJoinLateral(split('sentence) as('word, 'length))
      .select('sentence, 'word, 'length)
      .toAppendStream[(String, String, Int)]
      .print()

    tableEnvironment.registerFunction("split", split)
    tableEnvironment.createTemporaryView("words", env.fromElements("hello world"), 'sentence)
//    tableEnvironment.sqlQuery("select * from words ,lateral table(split(sentence)) as t(word, length)")
    tableEnvironment.sqlQuery("select * from words left join lateral table(split(sentence)) as t(word, length) on true")
      .toAppendStream[(String, String, Int)]
      .print()


    tableEnvironment.execute("table")
  }

  class Split extends TableFunction[(String, Int)] {
    def eval(sentence: String): Unit = {
      sentence.split(" ")
        .foreach(word => collect((word, word.length)))
    }
  }

}
