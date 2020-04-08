package com.hiscat.scala.basic

object Methods {
  def add(x: Int, y: Int): Int = x + y

  def addThenMultiply(x: Int, y: Int)(multiplier: Int): Int = (x + y) * multiplier

  def name: String = System.getProperty("user.name")

  def getSquareString(input: Double): String = {
    (input * input).toString
  }

  def main(args: Array[String]): Unit = {
    println(add(1, 2))
    println(addThenMultiply(1, 2)(3))
    println(name)
    println(getSquareString(4))
  }

}
