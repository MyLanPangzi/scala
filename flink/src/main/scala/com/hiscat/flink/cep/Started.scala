package com.hiscat.flink.cep

import java.util

import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala._

object Started {

  class Event(val id: Long, val name: String)

  case class SubEvent(override val id: Long, override val name: String, volume: Double) extends Event(id, name)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val stream = env.fromElements(
      new Event(42, "start"),
      SubEvent(42, "middle", 10.0),
      SubEvent(42, "end", 10.0)
    )

    CEP.pattern(
      stream,
      Pattern
        .begin[Event]("start").where(_.id == 42)
        .next("middle").subtype(classOf[SubEvent]).where(_.volume >= 10.0)
        .followedBy("end").where(_.name == "end"))
      .select(new PatternSelectFunction[Event, (Long, Double, String)] {
        override def select(pattern: util.Map[String, util.List[Event]]): (Long, Double, String) = {
          (pattern.get("start").get(0).id,
            pattern.get("middle").get(0).asInstanceOf[SubEvent].volume,
            pattern.get("end").get(0).asInstanceOf[SubEvent].name
          )
        }
      })
      .print()

    env.execute()

  }
}
