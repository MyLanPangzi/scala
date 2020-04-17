package com.hiscat.scala.collections

import scala.collection.mutable

object MutableIndexedSeqTest {
  def main(args: Array[String]): Unit = {
    val xs = mutable.IndexedSeq(1, 2, 3, 4, 5, 6)
    xs.mapInPlace(_ * 2)
    println(xs)
    xs.sortInPlace()
    println(xs)
    xs.sortInPlaceWith(_ > _)
    println(xs)
    xs.sortInPlaceBy(_ / 2)
    println(xs)
  }
}
