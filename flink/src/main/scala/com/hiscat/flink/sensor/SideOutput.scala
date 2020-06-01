package com.hiscat.flink.sensor

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.streaming.api.scala.{OutputTag, _}
import org.apache.flink.util.Collector

object SideOutput {

  val tag: OutputTag[SensorReading] = OutputTag[SensorReading]("low temperature")

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream = env.addSource(new SensorSource)
      .keyBy(_.id)
      .process(new KeyedProcessFunc)
    //      .process(new SideOutputProcessFunc)

    stream.getSideOutput(tag)
      .print()
    //    stream.print()

    env.execute()
  }

  class SideOutputProcessFunc extends ProcessFunction[SensorReading, SensorReading] {
    override def processElement(value: SensorReading,
                                ctx: ProcessFunction[SensorReading, SensorReading]#Context,
                                out: Collector[SensorReading]): Unit = {
      if (value.temperature < 30) {
        ctx.output(tag, value)
      } else {
        out.collect(value)
      }

    }
  }

  class KeyedProcessFunc extends KeyedProcessFunction[String, SensorReading, SensorReading] {

    override def processElement(value: SensorReading,
                                ctx: KeyedProcessFunction[String, SensorReading,
                                  SensorReading]#Context, out: Collector[SensorReading]): Unit = {
      if (value.temperature < 40) {
        ctx.output(tag, value)
      } else {
        out.collect(value)
      }
    }
  }

}
