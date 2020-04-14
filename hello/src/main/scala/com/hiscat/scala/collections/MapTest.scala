package com.hiscat.scala.collections

object MapTest {
  def main(args: Array[String]): Unit = {
    val map = collection.mutable.Map(
      "Hello" -> "World",
      "Hiscat" -> "BlueCat"
    )
    println(map)
    map += ("White" -> "Cat")
    println(map)
    map += ("Blue" -> "Cat", "a" -> "b")
    println(map)
    map ++= Map("c" -> "d")
    println(map)
    map -= "c"
    println(map)
    map --= List("a", "b", "c")
    println(map)
    map("a") = "b"
    println(map)
    for ((k, v) <- map) println(s"$k,$v")
    map.foreach {
      case (k, v) => println(s"$k,$v")
    }
  }
}
