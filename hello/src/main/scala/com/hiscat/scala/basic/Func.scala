package com.hiscat.scala.basic

object Func {
  def main(args: Array[String]): Unit = {
    val addOne: Int => Int = (x: Int) => x + 1
    println(addOne(1))
    println(addOne)
    val add: (Int, Int) => Int = (x: Int, y: Int) => x + y
    println(add(1, 2))
    val getInt: () => Int = () => 42
    println(getInt())
  }

}
