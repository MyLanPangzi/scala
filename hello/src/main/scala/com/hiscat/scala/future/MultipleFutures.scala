package com.hiscat.scala.future

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.TimeUnit

import scala.util.{Failure, Random, Success}

object MultipleFutures extends App {
  def getStockPrice(stockSymbol: String): Future[Double] = Future {
    var r = Random
    val d = r.nextDouble() * 1000
    Thread.sleep(r.nextInt(1000))
    d
  }

  def currentTime = System.currentTimeMillis()

  def deltaTime(t0: Long) = currentTime - t0

  def sleep(time: Long): Unit = Thread.sleep(time)

  val startTime = currentTime
  val aFuture = getStockPrice("A")
  val bFuture = getStockPrice("B")
  val cFuture = getStockPrice("C")
  val result: Future[(Double, Double, Double)] = for {
    a <- aFuture
    b <- bFuture
    c <- cFuture
  } yield (a, b, c)
  result.onComplete {
    case Success(value) => {
      val totalTime = deltaTime(startTime)
      println(s"The success case, time delta:$totalTime")
      println(s"The stock price are:$value")
    }
    case Failure(exception) => exception.printStackTrace()
  }
  sleep(5000)
}
