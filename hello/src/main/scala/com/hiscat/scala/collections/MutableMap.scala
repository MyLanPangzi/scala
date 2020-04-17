package com.hiscat.scala.collections

import scala.collection.mutable

object MutableMap {
  def main(args: Array[String]): Unit = {
    val ms = mutable.Map("a" -> 1, "b" -> 2)

    //Additions and Updates:
    println(ms("a") = 11)
    println(ms.addOne("c" -> 3))
    println(ms += ("d" -> 4))
    println(ms.addAll(Seq(("e" -> 5))) ++= Seq("f" -> 6))
    println(ms.put("g", 7))
    println(ms.getOrElseUpdate("h", 8))
    println()
    //Removals
    println(ms.subtractOne("h"))
    println(ms -= "g")
    println(ms.remove("f"))
    println(ms.filterInPlace((k, _) => k != "e"))
    ms.clear()
    println(ms)

    //Transformation
    println()
    ms.put("a", 1)
    println(ms.mapValuesInPlace((k, v) => v * 2))
    println(ms.clone())

    val fibs = fibFrom(1, 1).take(7)
    println(fibs.toList)
    val arr = scala.collection.immutable.ArraySeq(1, 2, 3)
    val arr2 = arr :+ 4
    println(arr2(0))
    val vec = scala.collection.immutable.Vector.empty
    val vec2 = vec :+ 1 :+ 2
    val vec3 = 100 +: vec2
    println(vec3(0))
  }

  def fibFrom(a: Int, b: Int): LazyList[Int] = a #:: fibFrom(b, a + b)
}
