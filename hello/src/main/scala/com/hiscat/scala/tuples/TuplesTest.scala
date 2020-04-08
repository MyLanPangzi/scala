package com.hiscat.scala.tuples

object TuplesTest {
  def main(args: Array[String]): Unit = {
    val tuple = ("Sugar", 25)
    println(tuple)
    println(tuple._1)
    println(tuple._2)
    val t: Tuple2[String, Int] = tuple
    println(t)
    val (name, quantity) = tuple
    println(name)
    println(quantity)
    val planets =
      List(("Mercury", 57.9), ("Venus", 108.2), ("Earth", 149.6),
        ("Mars", 227.9), ("Jupiter", 778.3))
    planets.foreach {
      case ("Earth", distance) =>
        println(s"Our planet is $distance million kilometers from the sun")
      case _ =>
    }
    val list = List((2, 4), (3, 1), (20, 10))
    for ((a, b) <- list) {
      println(a * b)
    }
  }

}
