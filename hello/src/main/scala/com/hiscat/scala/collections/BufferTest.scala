package com.hiscat.scala.collections

import scala.collection.mutable

object BufferTest {
  def main(args: Array[String]): Unit = {
    val buf = mutable.Buffer(1, 2, 3, 4, 5, 6)

    buf.append(7) += 8
    println(buf)

    buf.appendAll(Seq(9)) ++= Seq(10)
    println(buf)

    -1 +=: buf.prepend(0)
    println(buf)

    Seq(-3) ++=: buf.prependAll(mutable.Seq(-2))
    println(buf)

    buf.insert(buf.length, 11)
    println(buf)

    buf.insertAll(buf.length, mutable.Seq(12))
    println(buf)

    buf.padToInPlace(20, 0)
    println(buf)

    buf subtractOne -3
    println(buf)

    buf -= -2
    println(buf)

    buf subtractAll Seq(-1)
    println(buf)

    buf --= Seq(0)
    println(buf)

    buf remove 0
    println(buf)

    buf.remove(0, 1)
    println(buf)

    buf.trimStart(1)
    println(buf)

    buf.trimEnd(4)
    println(buf)

    buf.clear()
    println(buf)

    buf.patchInPlace(0, Seq(1, 2, 3), 3)
    println(buf)

    val clone = buf.clone()
    println(clone == buf)
    println(clone eq buf)
  }
}
