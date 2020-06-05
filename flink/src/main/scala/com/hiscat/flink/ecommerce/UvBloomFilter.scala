package com.hiscat.flink.ecommerce

import com.hiscat.flink.ecommerce.Uv.UvProcess
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object UvBloomFilter {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val path = "E:\\data\\WeChat Files\\geekXie\\FileStorage\\File\\2020-06\\UserBehavior.csv"
    env
      .readTextFile(path)
      .map(_.split(","))
      .map(strings => {
        UserBehavior(
          userId = strings(0).toLong,
          itemId = strings(1).toLong,
          categoryId = strings(2).toInt,
          behavior = strings(3),
          timestamp = strings(4).toLong
        )
      })
      .filter(_.behavior == "pv")
      .assignAscendingTimestamps(_.timestamp * 1000)
      .timeWindowAll(Time.hours(1))
      .process(new UvProcess)
      .print()


    env.execute()
  }


}
