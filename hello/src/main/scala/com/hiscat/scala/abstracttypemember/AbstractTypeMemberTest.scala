package com.hiscat.scala.abstracttypemember

object AbstractTypeMemberTest {
  def main(args: Array[String]): Unit = {
    val buf = newIntSeqBuf(7, 8)
    println("length = " + buf.length)
    println("content = " + buf.element)
  }

  abstract class Buffer[+T] {
    val element: T
  }

  abstract class SeqBuffer[U, +T <: Seq[U]] extends Buffer[T] {
    def length: Int = element.length
  }

  def newIntSeqBuf(e1: Int, e2: Int): SeqBuffer[Int, Seq[Int]] = new SeqBuffer[Int, Seq[Int]] {
    override val element: Seq[Int] = List(e1, e2)
  }

  //  trait Buffer {
  //    type T
  //    val element: T
  //  }
  //
  //  abstract class SeqBuffer extends Buffer {
  //    type U
  //    type T <: Seq[U]
  //
  //    def length = element.length
  //  }
  //
  //  abstract class IntSeqBuffer extends SeqBuffer {
  //    type U = Int
  //  }
  //
  //  def newIntSeqBuf(ele: Int, ele2: Int): IntSeqBuffer =
  //    new IntSeqBuffer {
  //      override type T = List[U]
  //      override val element = List(ele, ele2)
  //    }

}
