package com.hiscat.scala.collections

object MapTest2 {
  def main(args: Array[String]): Unit = {
    val ms = Map(1 -> "a", 2 -> "b", 3 -> "c")
    //Lookups
    println(ms.get(1))
    println(ms(1))
    println(ms getOrElse(4, "d"))
    println(ms.contains(1))
    println(ms.isDefinedAt(5))
    println()

    //Subcollections
    println(ms.keys.mkString(","))
    println(ms.keysIterator.mkString(","))
    println(ms.keySet)
    println(ms.values)
    println(ms.valuesIterator.mkString(","))

    //Transformation
    println(ms.view.filterKeys(_ > 1).mkString(","))
    println(ms.view.mapValues(_ * 2).mkString(","))
  }
}
