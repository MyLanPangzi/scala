package com.hiscat.scala.fp

object NullTest {
  def main(args: Array[String]): Unit = {
    println(toInt("1"))
    println(toInt("a"))
    toInt("10") match {
      case Some(i) => println(i)
      case None => println("Nothing")
    }
    val y = for {
      a <- toInt("1a")
      b <- toInt("1")
      c <- toInt("1")
    } yield a + b + c
    println(y)
    toInt("1").foreach(println)
    toInt("x").foreach(println)
  }

  def toInt(s: String): Option[Int] = {
    try {
      Some(Integer.parseInt(s.trim))
    } catch {
      case e: Exception => None
    }
  }
}
