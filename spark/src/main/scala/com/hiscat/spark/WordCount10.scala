package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


object WordCount10 {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("WordCount10")
    )
    val words = List("hello", "world", "hello", "scala", "hello", "hello", "hadoop")

    println(sc.makeRDD(words).groupBy(word => word).mapValues(_.size).collect().mkString(","))
    println(sc.makeRDD(words).map((_, 1)).reduceByKey(_ + _).collect().mkString(","))
    println(sc.makeRDD(words).map((_, 1)).groupByKey().mapValues(_.size).collect().mkString(","))
    println(sc.makeRDD(words).map((_, 1)).aggregateByKey(0)(_ + _, _ + _).collect().mkString(","))
    println(sc.makeRDD(words).map((_, 1)).foldByKey(0)(_ + _).collect().mkString(","))
    println(sc.makeRDD(words).map((_, 1)).combineByKey(v => v, (acc: Int, c: Int) => acc + c, (acc1: Int, acc2: Int) => acc1 + acc2).collect().mkString(","))
    println(sc.makeRDD(words).map((_, 1)).countByKey())
    println(sc.makeRDD(words).countByValue())
    println(sc.makeRDD(words).map(word => mutable.Map(word -> 1)).reduce((map1, map2) => {
      map2.foreach {
        case (key, i) => map1(key) = map1.getOrElse(key, 0) + i
      }
      map1
    }))

    println(
      sc.makeRDD(words).aggregate(mutable.HashMap[String, Int]())(
        (map: mutable.HashMap[String, Int], s: String) => {
          map(s) = map.getOrElse(s, 0) + 1
          map
        },
        (map1: mutable.HashMap[String, Int], map2: mutable.HashMap[String, Int]) => {
          map2.foreach {
            case (key, _) => map1(key) = map1.getOrElse(key, 0) + 1
          }
          map1
        }
      )
    )

    sc.stop()
  }
}
