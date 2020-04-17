package com.hiscat.scala.collections

object ImmutableSet {
  def main(args: Array[String]): Unit = {
    val xs = Set(1, 2, 3)
    //    Additions
    println(xs incl 4)
    println(xs + 4)

    //    Removals
    println(xs excl 3)
    println(xs - 3)
    println(xs removedAll Set(1, 2))
    println(xs -- Set(1, 2))
  }
}
