package com.hiscat.scala.collections

object MapMethodsTest {
  def main(args: Array[String]): Unit = {
    val m = Map(
      1 -> "a",
      2 -> "b",
      3 -> "c",
      4 -> "d"
    )
    for ((k, v) <- m) println(s"k:$k,v:$v")
    println(m.keys)
    println(m.values)
    println(m.contains(3))
    println(m.transform((k, v) => v.toUpperCase()))
    println(m.view.filterKeys(Set(2, 3)).toMap)
    println(m.take(2))

    val states = scala.collection.mutable.Map(
      "AL" -> "Alabama",
      "AK" -> "Alaska"
    )
    println(states += ("AZ" -> "Arizona"))
    println(states += ("CO" -> "Colorado", "KY" -> "Kentucky"))
    println(states -= "KY")
    println(states -= ("AZ", "CO"))
    println(states.filterInPlace((k, v) => k == "AK"))
    println(states)
  }
}
