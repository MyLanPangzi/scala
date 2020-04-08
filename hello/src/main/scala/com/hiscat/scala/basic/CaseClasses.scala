package com.hiscat.scala.basic

object CaseClasses {
  def main(args: Array[String]): Unit = {
    val point = Point(1, 2)
    val anotherPoint = Point(1, 2)
    val yetAnotherPoint = Point(2, 2)
    println(point == anotherPoint)
    println(point == yetAnotherPoint)
  }
}

case class Point(x: Int, y: Int)
