package com.hiscat.scala.collections


import scala.collection.SortedSet

object IterableTest {
  println("ss")

  def main(args: Array[String]): Unit = {
    val it = Iterable(1, 2, 3, 4, 5, 6)
    println(it.iterator.mkString)

    println(it.mkString(","))
    println(it.grouped(3).mkString(","))
    println(it.sliding(3).mkString(","))

    println(it.concat(Iterable(7, 8, 9)))

    println(it.map(_ * 2))
    println(Iterable(Iterable(1, 2), Iterable(3, 4)).flatMap(it => it.iterator))
    println(it.collect(_.toString))

    println(it.toArray.mkString(","))
    println(it.toList)
    println(it.toList eq it)
    //    println(it.toIterable)
    println(it.toSeq)
    println(it.toIndexedSeq)
    println(it.toSet)
    println(Iterable((1, 2), (3, 4)).toMap)
    println(it.to(SortedSet))

    val array = Array.fill(6)(0)
    it.copyToArray(array, 0, it.size)
    println(array.mkString(","))

    println(it.isEmpty)
    println(it.nonEmpty)
    println(it.size)
    println(it.knownSize)
    println(it.sizeCompare(6))
    println(it.sizeCompare(Iterable(1)))
    println(it.sizeIs < 7)

    println(it.head)
    println(it.headOption)
    println(it.last)
    println(it.lastOption)
    println(it.find(_ == 1))

    println(it.tail)
    println(it.init)
    println(it.slice(0, 2))
    println(it.take(2))
    println(it.takeWhile(_ < 3))
    println(it.takeRight(3))
    println(it.drop(2))
    println(it.dropWhile(_ < 3))
    println(it.dropRight(3))
    println(it.filter(_ % 2 == 0))
    println(it.withFilter(_ % 2 == 0).map(i => i))
    println(it.filterNot(_ % 2 == 0))

    println(it.splitAt(3))
    println(it.span(_ < 3))
    println(it.partition(_ % 2 == 0))
    println(it.groupBy(_ % 2 == 0))
    println(it.groupMap(_ % 2 == 0)(_ * 2))
    println(it.groupMapReduce(_ % 2 == 0)(_ * 2)(_ + _))

    println(it.forall(_ > 0))
    println(it.exists(_ > 3))
    println(it.count(_ > 3))

    println(it.foldLeft(1.0)(_ / _))
    println(it.foldRight(0)(_ * 2 + _))
    println(it.reduceLeft((acc, i) => {
      val r = acc + i * 2
      println(s"$i,$acc,${i * 2},$r")
      r
    }))
    println(it.reduceRight((i, acc) => {
      val r = acc + i * 2
      println(s"$i,$acc,${i * 2},$r")
      r
    }))

    println(it.sum)
    println(it.product)
    println(it.min)
    println(it.max)
    println(it.minOption)
    println(it.maxOption)

    println(it.addString(new StringBuilder(), "(", ",", ")"))
    println(it.mkString("(", ",", ")"))

    println(it zip Iterable(7, 8, 9))
    println(it.zipAll(Iterable(7, 8, 9), 1, 0))
    println(it.zipWithIndex)
    println(it.view)

  }
}
