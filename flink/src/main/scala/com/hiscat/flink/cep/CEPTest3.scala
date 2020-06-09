package com.hiscat.flink.cep

import java.util

import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.cep.{PatternFlatSelectFunction, PatternFlatTimeoutFunction}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import scala.collection.JavaConversions._

object CEPTest3 {

  case class OrderEvent(id: String, eventType: String, ts: Long)


  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val input = env.fromElements(
      OrderEvent("1", "create", 1000),
      OrderEvent("2", "create", 1000),
      OrderEvent("2", "pay", 2000)
    )
      .assignAscendingTimestamps(_.ts)
      .keyBy(_.id)

    val pattern = Pattern
      .begin[OrderEvent]("create").where(_.eventType == "create")
      .next("pay").where(_.eventType == "pay").within(Time.seconds(5))

    val tag = OutputTag[String]("not pay")
    val stream = CEP.pattern(input, pattern)
      .flatSelect(
        tag,
        new PatternFlatTimeoutFunction[OrderEvent, String] {
          override def timeout(pattern: util.Map[String, util.List[OrderEvent]], timeoutTimestamp: Long, out: Collector[String]): Unit = {
            out.collect(s"not pay order:${pattern("create").head.id}")
          }
        },
        new PatternFlatSelectFunction[OrderEvent, String] {
          override def flatSelect(pattern: util.Map[String, util.List[OrderEvent]], out: Collector[String]): Unit = {
            //            pattern.keySet().foreach(println(_))
            out.collect(s"order:${pattern("pay").head.id}")

          }
        })

    stream.print()
    stream.getSideOutput(tag).print()


    //    val stream = CEP.pattern(input, pattern)
    //      .flatSelect(OutputTag[String]("not pay"))((map: Map[String, Iterable[OrderEvent]], _, out: Collector[String]) => out.collect(s"pay order id:${map("create").head.id}"))((map: Map[String, Iterable[OrderEvent]], out: Collector[String]) => out.collect(s"not  pay order id:${map("create").head.id}"))
    //
    //    stream.print()
    //    stream.getSideOutput(OutputTag[String]("not pay"))
    //      .print()


    env.execute()
  }
}
