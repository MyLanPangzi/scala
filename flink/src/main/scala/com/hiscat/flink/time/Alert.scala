package com.hiscat.flink.time

import com.hiscat.flink.sensor.SensorReading
import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object Alert {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.addSource(new SensorSource)
      .keyBy(_.id)
      .process(new AlterProcessFunc)
      .print()

    env.execute()
  }

  class AlterProcessFunc extends KeyedProcessFunction[String, SensorReading, SensorReading] {

    lazy val tempState: ValueState[Double] =
      getRuntimeContext.getState(new ValueStateDescriptor[Double]("tempState", classOf[Double]))

    lazy val timerState: ValueState[Long] =
      getRuntimeContext.getState(new ValueStateDescriptor[Long]("timerState", classOf[Long]))

    override def processElement(value: SensorReading,
                                ctx: KeyedProcessFunction[String, SensorReading, SensorReading]#Context,
                                out: Collector[SensorReading]): Unit = {
      if (tempState.value() == 0.0 || tempState.value() < value.temperature) {
        ctx.timerService().deleteProcessingTimeTimer(timerState.value())
        timerState.clear()
      } else if (timerState.value() > value.temperature) {
        timerState.update(ctx.timerService().currentProcessingTime() + 1000)
        ctx.timerService().registerProcessingTimeTimer(timerState.value())
      }
      tempState.update(value.temperature)
//      out.collect(value)
    }

    override def onTimer(timestamp: Long,
                         ctx: KeyedProcessFunction[String, SensorReading, SensorReading]#OnTimerContext,
                         out: Collector[SensorReading]): Unit = {
      println(s"key:${ctx.getCurrentKey} $timestamp up !!!")
    }
  }

}
