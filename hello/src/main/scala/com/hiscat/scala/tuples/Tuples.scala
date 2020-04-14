package com.hiscat.scala.tuples

object Tuples {
  def main(args: Array[String]): Unit = {
    val d = ("Maggie", 30)
    println(d)
    val t = (3, "Three", Person("hello"))
    println(t)
    println(t._1)
    println(t._2)
    println(t._3)
    val (x, y, z) = t
    println(s"$x, $y, $z")
    val (symbol, curPrice, bidPrice) = getStockInfo
    println(s"$symbol,$curPrice,$bidPrice")
  }

  def getStockInfo: (String, Int, Double) = {
    ("NFLX", 100, 101.0)
  }

  case class Person(name: String)

}
