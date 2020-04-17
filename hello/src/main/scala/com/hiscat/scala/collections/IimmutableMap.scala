package com.hiscat.scala.collections

object IimmutableMap {
  def main(args: Array[String]): Unit = {
    val ms = Map("a" -> 1, "b" -> 2)
    //Additions and Updates:
    println(ms.updated("a", 2))
    println(ms + ("a" -> 2))

    //Removals
    println(ms.removed("a"))
    println(ms - "b")
    println(ms.removedAll(Seq("a")))
    println(ms -- Seq("b"))
  }
}
