package com.hiscat.scala.future

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

import scala.util.{Failure, Random, Success}

object FutureTest {
  def main(args: Array[String]): Unit = {
    val a = Future {
      TimeUnit.SECONDS.sleep(3)
      43
    }
    val b = a.map(_ * 2)
    b.onComplete {
      case Success(value) => println(value)
      case Failure(exception) => println(exception)
    }
    TimeUnit.DAYS.sleep(1)
  }

}
