package com.hiscat.flink.cep

import java.util
import java.util.Collections

import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object CEPTest {

  case class Event(username: String, eventType: String, ip: String)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)


    val stream = env.fromElements(
      Event("1", "fail", "192.1.1.2"),
      Event("1", "fail", "192.1.1.3"),
      Event("1", "fail", "192.1.1.4"),
      Event("1", "success", "192.1.1.5")
    ).keyBy(_.username)

    val pattern = Pattern
      .begin[Event]("begin").where(_.eventType == "fail")
      .next("fail1").where(_.eventType == "fail")
      .next("fail2").where(_.eventType == "fail")
      .within(Time.seconds(10))

    val loginStream = CEP.pattern(stream, pattern)

    loginStream
      .process(new PatternProcessFunction[Event, (String, String, String)] {
      override def processMatch(`match`: util.Map[String, util.List[Event]],
                                ctx: PatternProcessFunction.Context,
                                out: Collector[(String, String, String)]): Unit = {
        val ip1 = `match`.getOrDefault("begin", Collections.emptyList()).get(0).ip
        val ip2 = `match`.getOrDefault("fail1", Collections.emptyList()).get(0).ip
        val ip3 = `match`.getOrDefault("fail2", Collections.emptyList()).get(0).ip
        out.collect((ip1, ip2, ip3))
      }
    })
      .print()

    env.execute()
  }
}
