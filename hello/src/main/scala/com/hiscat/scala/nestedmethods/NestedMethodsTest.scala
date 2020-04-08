package com.hiscat.scala.nestedmethods

object NestedMethodsTest {
  def main(args: Array[String]): Unit = {
    println(factorial(3))
  }

  def factorial(x: Int): Int = {
    @scala.annotation.tailrec
    def fact(x: Int, acc: Int): Int = {
      if (x <= 1) acc else fact(x - 1, x * acc)
    }

    fact(x, 1)
  }
}
