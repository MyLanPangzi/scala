package com.hiscat.spark

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object Transformations {
  def main(args: Array[String]): Unit = {
    val sc = SparkContext.getOrCreate(
      new SparkConf()
        .setAppName("Transformations ")
        .setMaster("local[*]")
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4))
        .map(_ * 2)
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4), 2)
        .mapPartitions(it => Iterator(it.max))
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4), 2)
        .mapPartitionsWithIndex((i, it) => if (i == 0) Iterator.empty else Iterator(it.max))
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4))
        .filter(_ > 2)
        .sum()
    )
    println(
      sc
        .makeRDD(List(List(1), List(2)))
        .flatMap(it => it)
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4))
        .sample(withReplacement = false, 0.3)
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4))
        .sample(withReplacement = true, 2)
        .sum()
    )

    println(
      sc
        .makeRDD(List(1, 2, 3, 4))
        .union(sc.makeRDD(List(5)))
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4))
        .intersection(sc.makeRDD(List(1, 2, 3)))
        .sum()
    )
    println(
      sc
        .makeRDD(List(1, 2, 3, 4, 1, 2, 3, 4))
        .distinct()
        .sum()
    )
    println(
      sc
        .makeRDD(List("Hello", "Hello", "World", "Spark", "Scala"))
        .groupBy(k => k)
        .map {
          case (key, strings) => (key, strings.map(_.length).sum)
        }
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List("Hello", "Hello", "World", "Spark", "Scala"))
        .map(s => (s, 1))
        .reduceByKey(_ + _)
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List((false, 1), (true, 2), (false, 3), (true, 4), (false, 5), (true, 6), (false, 7), (true, 8)), 2)
        .aggregateByKey(1)(_ + _, _ + _)
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List("b", "a", "c", "spark"), 1)
        .map(s => (s, s.length))
        .sortByKey()
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List((1, 2), (2, 3)))
        .join(sc.makeRDD(List((1, 3), (2, 4), (3, 4))))
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List((1, 2), (2, 3), (4, 5)))
        .leftOuterJoin(sc.makeRDD(List((1, 3), (2, 4), (3, 4))))
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List((1, 2), (2, 3), (4, 5)))
        .rightOuterJoin(sc.makeRDD(List((1, 3), (2, 4), (3, 4))))
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List((1, 2), (2, 3), (4, 5)))
        .fullOuterJoin(sc.makeRDD(List((1, 3), (2, 4), (3, 4))))
        .collect()
        .mkString(",")
    )
    println(
      sc
        .makeRDD(List((1, 2)))
        .cogroup(sc.makeRDD(List((1, 3))))
        .collect()
        .mkString(",")
    )

    println(
      sc.makeRDD(List(1, 2))
        .cartesian(sc.makeRDD(List(3, 4)))
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2))
        .pipe("D:\\soft\\Git\\usr\\bin\\cat.exe")
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .coalesce(2)
        .mapPartitionsWithIndex((i, it) => it.map(_ * i))
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2, 3, 4))
        .repartition(1)
        .mapPartitionsWithIndex((i, it) => it.map(_ * i))
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List((1, 1), (2, 2), (3, 3), (4, 4)))
        .repartitionAndSortWithinPartitions(new Partitioner {
          override def numPartitions: Int = 2

          override def getPartition(key: Any): Int = key match {
            case x: Int => if (x % 2 == 0) 0 else 1
          }
        })
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List(1, 2, 3))
        .subtract(sc.makeRDD(List(1, 2)))
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List((1, 1), (2, 2), (3, 3), (4, 4)))
        .partitionBy(new Partitioner {
          override def numPartitions: Int = 2

          override def getPartition(key: Any): Int = key match {
            case x: Int => if (x % 3 == 0) 0 else 1
          }
        })
        .mapPartitions(it => Iterator(it.mkString(",")))
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List((1, 2), (1, 3), (2, 4), (2, 5)), 2)
        .foldByKey(1)(_ + _)
        .collect()
        .mkString(",")
    )
    println(
      sc.makeRDD(List((1, 1), (1, 2), (2, 3), (2, 4)))
        .combineByKey(
          i => i,
          (acc: Int, i) => acc + i,
          (acc1: Int, acc2: Int) => acc1 + acc2
        )
        .collect()
        .mkString(",")
    )
    sc.stop()

  }
}
