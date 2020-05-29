package com.hiscat.flink.connected

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.util.Collector

object ConnectedStream {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val control = env.fromElements("DROP", "IGNORE").keyBy(x => x)
    val word = env.fromElements("Apache", "DROP", "Flink", "IGNORE").keyBy(x => x)
    control.connect(word)
      .flatMap(new ControlFunction)
      .print()

    env.execute()
  }

  class ControlFunction extends RichCoFlatMapFunction[String, String, String] {

    private var blocked: ValueState[Boolean] = _;

    override def open(parameters: Configuration): Unit = {
      blocked = getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("Blocked", classOf[Boolean]))
    }

    override def flatMap1(value: String, out: Collector[String]): Unit = {
      blocked.update(true)
    }

    override def flatMap2(value: String, out: Collector[String]): Unit = {
      if (blocked.value() == null) {
        out.collect(value)
      }
    }
  }

}
