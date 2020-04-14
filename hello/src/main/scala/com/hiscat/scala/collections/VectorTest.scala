package com.hiscat.scala.collections

object VectorTest {
  def main(args: Array[String]): Unit = {
    val vec = Vector(1, 2, 3, 4, 5)
    println(vec)
    println(vec :+ 6)
    println(0 +: vec)
    println(Vector(-1, 0) ++: vec)
    println(vec ++: Vector(6, 7))
    for (i <- vec) println(i)
  }
}
