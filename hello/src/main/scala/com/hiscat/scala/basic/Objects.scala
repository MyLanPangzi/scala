package com.hiscat.scala.basic

object Objects {
  def main(args: Array[String]): Unit = {
    println(IdFactory.create())
    println(IdFactory.create())
  }

}

object IdFactory {
  private var counter = 0

  def create(): Int = {
    counter += 1
    counter
  }
}