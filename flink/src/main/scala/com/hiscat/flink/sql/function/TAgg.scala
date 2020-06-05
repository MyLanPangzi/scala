package com.hiscat.flink.sql.function

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.api.common.typeinfo.{TypeHint, TypeInformation}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.functions.TableAggregateFunction
import org.apache.flink.table.functions.TableAggregateFunction.RetractableCollector
import org.apache.flink.table.planner.{JDouble, JInt}
import org.apache.flink.util.Collector

object TAgg {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val tableEnvironment = StreamTableEnvironment.create(env)

    val top2Temp = new Top2Temp
    tableEnvironment.fromDataStream(
      env.addSource(new SensorSource),
      'id, 'timestamp, 'temperature as 'temp
    )
      .groupBy('id)
      .flatAggregate(top2Temp('temp) as('temp, 'rank))
      .select('id, 'temp, 'rank)
      .toRetractStream[(String, Double, Int)]
      .print()

    tableEnvironment.execute("UDTAGG")
  }

  case class Top2Accum(var first: Double, var second: Double)

  class Top2Temp extends TableAggregateFunction[(Double, Int), Top2Accum] {
    override def createAccumulator(): Top2Accum = Top2Accum(0.0, 0.0)

    def accumulate(acc: Top2Accum, v: Double) {
      if (v > acc.first) {
        acc.second = acc.first
        acc.first = v
      } else if (v > acc.second) {
        acc.second = v
      }
    }

    def emitValue(acc: Top2Accum, out: Collector[(Double, Int)]): Unit = {
      // emit the value and rank
      if (acc.first != Int.MinValue) {
        out.collect((acc.first, 1))
      }
      if (acc.second != Int.MinValue) {
        out.collect((acc.second, 2))
      }
    }

//    def emitUpdateWithRetract(
//                               acc: Top2Accum,
//                               out: RetractableCollector[(Double, Int)])
//    : Unit = {
//      if (acc.first != acc.oldFirst) {
//        // if there is an update, retract old value then emit new value.
//        if (acc.oldFirst != Int.MinValue) {
//          out.retract(JTuple2.of(acc.oldFirst, 1))
//        }
//        out.collect(JTuple2.of(acc.first, 1))
//        acc.oldFirst = acc.first
//      }
//      if (acc.second != acc.oldSecond) {
//        // if there is an update, retract old value then emit new value.
//        if (acc.oldSecond != Int.MinValue) {
//          out.retract(JTuple2.of(acc.oldSecond, 2))
//        }
//        out.collect(JTuple2.of(acc.second, 2))
//        acc.oldSecond = acc.second
//      }
//    }

    //List of column aliases must have same degree as table;
    // the returned table of function 'com.hiscat.flink.sql.function.TAgg$Top2Temp' has 1 columns, whereas alias list has 2 columns
    //    override def getResultType: TypeInformation[(Double, Int)] = TypeInformation.of(new TypeHint[(Double, Int)] {})
    //    override def getAccumulatorType: TypeInformation[Top2Accum] = TypeInformation.of(classOf[Top2Accum])

    def merge(acc: Top2Accum, its: Iterable[Top2Accum]): Unit = {
      val iter = its.iterator
      while (iter.hasNext) {
        val top2 = iter.next()
        accumulate(acc, top2.first)
        accumulate(acc, top2.second)
      }
    }
  }

}
