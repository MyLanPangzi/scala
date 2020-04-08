package com.hiscat.scala.mixins

object MixinsTest {
  def main(args: Array[String]): Unit = {
    val d = new D
    println(d.message)
    println(d.loudMessage)
    val iterator = new StringIterator("Hello")
    println(iterator.next())
    println(iterator.next())

    val iter = new RichStringIter
    iter.foreach(println)
  }

  class RichStringIter extends StringIterator("Scala") with RichIterator

  trait RichIterator extends AbsIterator {
    def foreach(f: T => Unit): Unit = while (hasNext) f(next())
  }

  class StringIterator(s: String) extends AbsIterator {
    override type T = Char
    private var i: Int = 0

    override def hasNext: Boolean = i < s.length

    override def next(): Char = {
      val ch = s charAt i
      i += 1
      ch
    }
  }

  abstract class AbsIterator {
    type T

    def hasNext: Boolean

    def next(): T
  }

  abstract class A {
    val message: String
  }

  class B extends A {
    val message: String = "bbbb"
  }

  trait C extends A {
    def loudMessage: String = message.toUpperCase()
  }

  class D extends B with C

}
