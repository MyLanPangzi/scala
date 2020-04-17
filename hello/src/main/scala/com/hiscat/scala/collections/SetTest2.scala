package com.hiscat.scala.collections

object SetTest2 {
  def main(args: Array[String]): Unit = {
    val xs = Set(1, 2, 3)

    //    Tests
    println(xs.contains(1))
    println(xs(1))
    println(xs.subsetOf(Set(1, 2, 3, 4)))

    //Addition
    println(xs concat Seq(4) ++ Seq(5))

    //Removal:
    println(xs.empty)

    //    Binary Operations:
    println(xs intersect Set(1, 2))
    println(xs & Set(1, 2))
    println(xs | Set(4))
    println(xs union Set(4))
    println(xs &~ Set(4))
    println(xs diff Set(4))

  }
}
