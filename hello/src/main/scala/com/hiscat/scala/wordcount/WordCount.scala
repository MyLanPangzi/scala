package com.hiscat.scala.wordcount

import scala.io.Source
import scala.collection.mutable

object WordCount {
  def main(args: Array[String]): Unit = {
    val source = Source.fromFile("input/word.txt")
    source
      .getLines()
      .flatMap(_.split(" "))
      .toList.view
      .groupBy(word => word)
      .map {
        case (key, value) => (key, value.size)
      }
      .toList.view
      .sortWith((lt, gt) => lt._2 > gt._2)
      .take(3)
      .foreach(println)
    source.close()
    List(("Hello Scala", 4), ("Hello Spark", 2))
      .map(_._2)
      .sortWith((gt, lt) => gt > lt)
      .take(3)
      .foreach(println)
    List(("Hello Scala", 4), ("Hello Spark", 2))
      //      .map(_._2)
      .sortBy {
        case (str, i) => i
      }
      .reverse
      .map {
        case (str, i) => i
      }
      .take(3)
      .foreach(println)
  }
}
