package com.hiscat.scala.annotations

object AnnotationsTest {
  def main(args: Array[String]): Unit = {
    hello
    println(fac(3))
  }

  def fac(x: Int): Int = {
    @scala.annotation.tailrec
    def helper(x: Int, acc: Int = 1): Int = {
            if (x == 1) acc else helper(x - 1, acc * x)
//      if (x == 1) acc else x * helper(x - 1)
    }

    helper(x)
  }

  @deprecated("deprecation message", "release # which deprecates method")
  def hello = "hello"
}
