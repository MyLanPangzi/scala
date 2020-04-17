package com.hiscat.scala.collections

object ViewTest {
  def main(args: Array[String]): Unit = {
    val v = Vector(1 to 10: _*)
    println(v.map(_ + 1).map(_ * 2))
    println(v.view.map(_ + 1).map(_ * 2).to(List))
    println(v.view)
  }
}
