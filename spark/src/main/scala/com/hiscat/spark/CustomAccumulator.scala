package com.hiscat.spark

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object CustomAccumulator {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("CustomAccumulator")
        .setMaster("local[*]")

    )

    val acc = new WordCountAccumulator
    sc.register(acc)
    acc.add("hello")
    sc.makeRDD(List("hello", "world", "hello", "scala"))
      .foreach(acc.add)
    println(acc.value)
    println(acc.copy().value)

    sc.stop()
  }

  class WordCountAccumulator extends AccumulatorV2[String, mutable.Map[String, Int]] {
    private var result: mutable.Map[String, Int] = new mutable.HashMap[String, Int]()

    override def isZero: Boolean = result.isEmpty

    override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = {
      val r = new WordCountAccumulator
      r.result = result.clone()
      r
    }

    override def reset(): Unit = result.clear()

    override def add(v: String): Unit = result(v) = result.getOrElse(v, 0) + 1


    override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit =
      other.value.foreach {
        case (str, i) => result(str) = result.getOrElse(str, 0) + i
      }

    override def value: mutable.Map[String, Int] = result
  }

}
