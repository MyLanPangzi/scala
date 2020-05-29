package com.hiscat.flink.sensor

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import scala.util.Random

object SensorTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.addSource(new SensorSource())
      .map(_.id)

      .print()

    env.execute()
  }


  class SensorSource extends RichParallelSourceFunction[SensorReading] {
    private var running = true;

    override def run(ctx: SourceFunction.SourceContext[SensorReading]): Unit = {
      val random = new Random()
      val seq = (1 to 10).map(
        i => ("sensor_" + (this.getRuntimeContext.getIndexOfThisSubtask * 10 + i), 65 + (random.nextGaussian() * 20))
      )

      while (running) {

        val millis = System.currentTimeMillis()

        seq.map(t => (t._1, t._2 + (random.nextGaussian() * .5)))
          .foreach(e => ctx.collect(SensorReading(e._1, millis, e._2)))

        Thread.sleep(100)
      }
    }

    override def cancel(): Unit = running = false
  }

}
