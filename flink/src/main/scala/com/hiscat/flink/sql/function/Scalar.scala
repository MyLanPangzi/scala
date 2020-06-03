package com.hiscat.flink.sql.function

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.functions.ScalarFunction

object Scalar {


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    val doubleAge = new DoubleAge
    val person = tableEnvironment.fromDataStream(
      env.fromElements(Person("hello", 10), Person("world", 20)),
      'name, 'age
    )
    person
      .select('name, doubleAge('age))
      .toAppendStream[(String, Int)]
      .print()

    tableEnvironment.registerFunction("double_age", doubleAge)
    tableEnvironment
      .sqlQuery("select name,double_age(age) from " + person)
      .toAppendStream[(String, Int)]
      .print()


    tableEnvironment.execute("sql")
  }

  class DoubleAge extends ScalarFunction {
    def eval(age: Int): Int = age * 2
  }

}
