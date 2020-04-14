package com.hiscat.scala.collections

object AnonymousFuncTest {
  def main(args: Array[String]): Unit = {
    println(List.range(1, 20))
    val ints = List(1, 2, 3)
    println(ints)
    println(ints.map(_ * 2))
    println(List.range(1, 10).filter(_ > 2))
    println(List.range(1, 10).filter(_ % 2 == 0))
    println(List.range(1, 10).filterNot(_ % 2 == 0))
    val s = new S
  }

  class P(name: String) {
    println("111")

    def this() {
      this("")
      println("222")
    }
  }

  class S(name: String) extends P {
    println("333")

    def this() {
      this("")
      println("444")
    }
  }

}
