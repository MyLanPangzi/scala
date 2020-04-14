package com.hiscat.scala.fp

object PureFuncTest {
  def main(args: Array[String]): Unit = {
    def double: Int => Int = _ * 2

  }

  def sum(list: List[Int]): Int = list match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }

}
