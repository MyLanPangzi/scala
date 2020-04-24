package com.hiscat.spark

import org.apache.spark.{SparkConf, SparkContext}

object ExternalDatasets {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("ExternalDatasets")
        .setMaster("local[*]")
    )
    println(sc
      .textFile("input")
      .map(_.length)
      .sum() / 1024)
    println(
      sc
        .wholeTextFiles("input")
        .map {
          case (name, content) =>
            println(name)
            content.length
        }
        .sum() / 1024
    )
    //    println(
    //      sc
    //        .sequenceFile("input", classOf[String], classOf[String])
    //        .map {
    //          case (str, content) => content.length
    //        }
    //        .sum()
    //    )

    //    sc
    //        .newAPIHadoopFile("")

//    val persist = sc
//      .textFile("input")
//      .persist()
    //sc
    //    .textFile("input")
    //    .map(f)

    sc.stop()
  }

  def f(s: String): Int = s.length
}
