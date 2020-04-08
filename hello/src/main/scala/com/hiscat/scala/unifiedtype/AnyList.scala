package com.hiscat.scala.unifiedtype

object AnyList {
  def main(args: Array[String]): Unit = {
    val list = List("s", 1, 'c', true, (), () => "s")
    list.foreach { e => println(e) }
  }

}
