package com.hiscat.scala.collections

import scala.collection.mutable

object MutableSet {
  def main(args: Array[String]): Unit = {
    val xs = mutable.Set(1, 2, 3)
    //    Additions
    xs addOne 4
    println(xs)
    xs += 5
    println(xs)
    xs ++= Set(6)
    println(xs)
    xs addAll Set(7)
    println(xs)
    println(xs add 3)

    //    Removals
    xs subtractOne 1
    println(xs)
    xs -= 2
    println(xs)
    xs subtractAll Set(3)
    println(xs)
    xs --= Set(4)
    println(xs)
    println(xs remove 5)
    xs.filterInPlace(_ > 6)
    println(xs)
    xs.clear()
    println(xs)

    //Update
    xs(1) = true
    println(xs)

    println(xs.clone())
  }
}
