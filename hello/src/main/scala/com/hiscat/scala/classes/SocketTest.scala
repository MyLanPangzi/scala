package com.hiscat.scala.classes

object SocketTest {
  def main(args: Array[String]): Unit = {
    println(new Socket())
    println(new Socket(1000))
    println(new Socket(1000, 5000))
    println(new Socket(timeout = 2000, linger = 6000))
    println(new Socket(linger = 7000))
  }

}
