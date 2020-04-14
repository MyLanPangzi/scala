package com.hiscat.scala.collections

object ListTest {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3)
    println(list)
    println(0 +: list)
    println(List(-1, 0) ++: list)
    for (i <- list) println(i)
    println(1 :: 2 :: 3 :: Nil)
  }
}
