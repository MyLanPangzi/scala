package com.hiscat.scala.bynamedparameter

object ByNamedParameterTest {


  def main(args: Array[String]): Unit = {
    println(calc(3))
    var i = 2
    whileLoop(i > 0) {
      println(i)
      i -= 1
    }
  }

  @scala.annotation.tailrec
  def whileLoop(condition: => Boolean)(body: => Unit): Unit =
    if (condition) {
      body
      whileLoop(condition)(body)
    }

  def calc(input: => Int): Int = input * 37
}
