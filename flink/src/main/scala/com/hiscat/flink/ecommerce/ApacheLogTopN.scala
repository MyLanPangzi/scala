package com.hiscat.flink.ecommerce

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

object ApacheLogTopN {

  case class ApacheLogEvent(ip: String,
                            userId: String,
                            eventTime: Long,
                            method: String,
                            url: String)

  case class UrlViewCount(url: String,
                          windowEnd: Long,
                          count: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    //ApacheLogEvent(linearray(0),
    //                       linearray(2),
    //                       timestamp,
    //                       linearray(5),
    //                       linearray(6))
    //83.149.9.216 - - 17/05/2015:10:05:03 +0000 GET /presentations/logstash-monitorama-2013/images/kibana-search.png
    env
      .readTextFile("E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\apachelog.txt")
      .map(_.split(" "))
      .map(strings => {
        val simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
        val timestamp = simpleDateFormat.parse(strings(3)).getTime
        ApacheLogEvent(
          ip = strings(0),
          userId = strings(2),
          eventTime = timestamp,
          method = strings(5),
          url = strings(6)
        )
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ApacheLogEvent](Time.seconds(3)) {
        override def extractTimestamp(element: ApacheLogEvent): Long = element.eventTime
      })
      .keyBy(_.url)
      .timeWindow(Time.minutes(1), Time.seconds(5))
      .aggregate(
        new UrlProcess,
        (k, w, it: Iterable[Long], out: Collector[UrlViewCount]) => out.collect(UrlViewCount(k, w.getEnd, it.head))
      )
      .keyBy(_.windowEnd)
      .process(new TopN(3))
      .print()

    env.execute()
  }


  class TopN(top: Int) extends KeyedProcessFunction[Long, UrlViewCount, String] {
    lazy val urls: ListState[UrlViewCount] =
      getRuntimeContext.getListState(new ListStateDescriptor[UrlViewCount]("urls", classOf[UrlViewCount]))

    override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, UrlViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {
      import scala.collection.JavaConversions._
      val result = urls.get().toList
        .sortBy(-_.count)
        .take(top)
        .map(url => (url.count, url.url))
        .mkString(",")
      val str = Instant.ofEpochMilli(timestamp - 100).atZone(ZoneId.systemDefault()).toLocalDateTime + ": " + result
      out.collect(str)

      urls.clear()
    }

    override def processElement(value: UrlViewCount,
                                ctx: KeyedProcessFunction[Long, UrlViewCount, String]#Context,
                                out: Collector[String]): Unit = {
      urls.add(value)
      ctx.timerService().registerEventTimeTimer(value.windowEnd + 100)

    }
  }

  class UrlProcess extends AggregateFunction[ApacheLogEvent, Long, Long] {
    override def createAccumulator(): Long = 0

    override def add(value: ApacheLogEvent, accumulator: Long): Long = accumulator + 1

    override def getResult(accumulator: Long): Long = accumulator

    override def merge(a: Long, b: Long): Long = a + b
  }

}
