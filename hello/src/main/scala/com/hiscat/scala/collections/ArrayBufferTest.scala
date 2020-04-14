package com.hiscat.scala.collections

import scala.collection.mutable.ArrayBuffer

object ArrayBufferTest {
  def main(args: Array[String]): Unit = {
    val ints = ArrayBuffer[Int]()
    println(ints += 1)
    println(ints += 2)
    println(ints.append(3))
    println(ints.prepend(0))
    ints.insert(ints.size, ints.size)
    println(ints)
    println(ints += 5 += 6)
    println(ints ++= List(7, 8, 9))
    println(ints -= 9)
    println(ints --= Array(7, 8))

    val strings = ArrayBuffer[String]()
    println(strings.append("Hello"))
    println(strings.appendAll(Seq("World")))
    strings.insert(strings.size, "Scala")
    println(strings)
    strings.insertAll(strings.size, Vector("Hiscat"))
    println(strings)

    val c = ArrayBuffer.range('a', 'h')
    println(c)
    println(c.remove(0))
    c.trimStart(2)
    println(c)
    c.trimEnd(2)
    println(c)
  }
}
