package com.hiscat.scala.tryseccuessfailure

import scala.util.{Failure, Success, Try}

object CaseObjectTest {
  def main(args: Array[String]): Unit = {
    println(toInt("1"))
    println(toInt("boo"))
    toInt("a") match {
      case Success(value) => println(value)
      case Failure(exception) => println(exception)
    }
    val y = for {
      a <- toInt("1")
      b <- toInt("1")
      c <- toInt("a")
    } yield a + b + c
    println(y)
  }

  def toInt(s: String): Try[Int] = Try(Integer.parseInt(s))
}
