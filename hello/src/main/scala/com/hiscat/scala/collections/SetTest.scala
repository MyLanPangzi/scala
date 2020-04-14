package com.hiscat.scala.collections

import scala.collection.mutable

object SetTest {
  def main(args: Array[String]): Unit = {
    val set = mutable.Set(1, 2, 3)
    println(set)
    println(set += 0)
    println(set += -1 += -2)
    println(set ++= Vector(4, 5))
    println(set -= 1 --= List(-1, -2, 2, 3, 0))
    set.remove(4)
    println(set)
    set.clear()
    println(set)
  }
}
