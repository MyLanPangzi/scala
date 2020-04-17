package com.hiscat.scala.collections

import scala.collection.BitSet
import scala.collection.immutable.TreeSet

object SortedSet {
  def main(args: Array[String]): Unit = {
    val ordering = Ordering.fromLessThan[String](_ > _)
    val set = TreeSet.empty(ordering)
    println(set)
    val xs = TreeSet.empty[String]
    println(xs)
    val xs2 = xs + "one" + "two" + "three" + "four"
    println(xs2)
    println(xs2.range("one", "two"))
    println(xs2.rangeFrom("onw"))
    println(BitSet(1, 2, 3, 4))

    val s = new S
    s.test()
  }

  trait P {
    var a: Int = 20

    def test(): Unit = println(a)
  }

  class S extends P {
//    override val a: Int = 0
  }

}
