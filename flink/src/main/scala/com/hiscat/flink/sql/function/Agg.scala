package com.hiscat.flink.sql.function

import com.hiscat.flink.sql.function.Scalar.DoubleAge
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.functions.AggregateFunction
import org.apache.flink.table.planner.{JDouble, JInt, JLong}

object Agg {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    val person = tableEnvironment.fromDataStream(
      env.fromElements(Person("hello", 10), Person("world", 20)),
      'name, 'age
    )
    val avg = new AgeAvg
    person
      .select(avg('age))
      .toRetractStream[Double]
      .print()

    tableEnvironment.registerFunction("age_avg", avg)
    tableEnvironment
      .sqlQuery("select age_avg(age) from " + person)
      .toRetractStream[Double]
      .print()


    tableEnvironment.execute("UDAgg")
  }

  case class AgeAvgAcc(var count: JInt, var age: JInt)

  class AgeAvg extends AggregateFunction[JDouble, AgeAvgAcc] {
    override def getValue(accumulator: AgeAvgAcc): JDouble = {
      accumulator.age / accumulator.count.toDouble
    }

    override def createAccumulator(): AgeAvgAcc = AgeAvgAcc(0, 0)

    def accumulate(accumulator: AgeAvgAcc, age: JInt): Unit = {
      accumulator.count = accumulator.count + 1
      accumulator.age = accumulator.age + age
    }
  }

}
