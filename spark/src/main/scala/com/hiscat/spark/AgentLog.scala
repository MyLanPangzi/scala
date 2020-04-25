package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable

object AgentLog {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("AgentLog")
        .setMaster("local[*]")
    )

    //1)数据准备
    //agent.log：时间戳，省份，城市，用户，广告，中间字段使用空格分隔。
    //2)需求描述
    //统计出每一个省份广告被点击数量排行的Top3
    //3)需求分析
    //省份 广告 数量
    //省份1 广告 数量1
    //省份2 广告 数量2
    //省份3 广告 数量3
    //4)功能实现
    sc.textFile("input/agent.log")
      .map(_.split(" "))
      .map(line => ((line(1), line(4)), 1))
      .reduceByKey(_ + _)
      .map(kv => (kv._1._1, (kv._1._2, kv._2)))
      .groupBy(kv => kv._1)
      .map(kv => (kv._1, kv._2.map(kv => kv._2).toList.sortWith((lt, gt) => lt._2 > gt._2).take(3)))
      .collect()
      .foreach(println)

    sc.stop()
  }
}
