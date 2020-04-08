package com.hiscat.scala.defaultparamval

object DefaultParamValTest {
  def main(args: Array[String]): Unit = {
    log("hello")
    log("world", "WARNING")
    val point = new Point(y = 1)
    println(point.y)
  }

  def log(message: String, level: String = "INFO"): Unit = println(s"$level: $message")

  class Point(val x: Double = 0, val y: Double = 0)

}
