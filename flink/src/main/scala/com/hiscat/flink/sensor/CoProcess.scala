package com.hiscat.flink.sensor

import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object CoProcess {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val switch = env.fromElements(
      ("sensor_2", 10 * 1000L),
      ("sensor_7", 20 * 1000L)
    )
    env.addSource(new SensorSource)
      .keyBy(_.id)
      .connect(switch.keyBy(_._1))
      .process(new CoProcessFunc)
      .print()

    env.execute()
  }

  class CoProcessFunc extends KeyedCoProcessFunction[String, SensorReading, (String, Long), SensorReading] {

    lazy val forwardState: ValueState[Boolean] =
      getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("forwardState", classOf[Boolean]))

    override def processElement1(value: SensorReading,
                                 ctx: KeyedCoProcessFunction[String, SensorReading, (String, Long),
                                   SensorReading]#Context, out: Collector[SensorReading]): Unit = {
      if (forwardState.value()) {
        out.collect(value)
      }
    }

    override def processElement2(value: (String, Long),
                                 ctx: KeyedCoProcessFunction[String, SensorReading, (String, Long),
                                   SensorReading]#Context, out: Collector[SensorReading]): Unit = {
      forwardState.update(true)
      ctx.timerService().registerProcessingTimeTimer(ctx.timerService().currentProcessingTime() + value._2)
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedCoProcessFunction[String, SensorReading, (String, Long),
                           SensorReading]#OnTimerContext, out: Collector[SensorReading]): Unit = {
      forwardState.clear()
    }
  }

}
