package com.hiscat.scala.lowertypebound

object LowerTypeBoundTest {
  def main(args: Array[String]): Unit = {
    val african = ListNode[AfricanSwallow](AfricanSwallow(), Nil())
    val list: Node[Bird] = african
    list.prepend(EuropeanSwallow())
    println(list.prepend(EuropeanSwallow()))
  }

  trait Bird

  case class AfricanSwallow() extends Bird

  case class EuropeanSwallow() extends Bird
  trait Node[+B] {
    def prepend[U >: B](elem: U): Node[U]
  }

  case class ListNode[+B](h: B, t: Node[B]) extends Node[B] {
    def prepend[U >: B](elem: U): ListNode[U] = ListNode(elem, this)
    def head: B = h
    def tail: Node[B] = t
  }

  case class Nil[+B]() extends Node[B] {
    def prepend[U >: B](elem: U): ListNode[U] = ListNode(elem, this)
  }
//  trait Node[+B] {
//    def prepend[U >: B](elem: U): Node[U]
//  }
//
//  case class ListNode[+B](h: B, t: Node[B]) extends Node[B] {
//    override def prepend[U >: B](elem: U): ListNode[U] = ListNode(elem, this)
//
//    def head: B = h
//
//    def tail: Node[B] = t
//  }
//
//  case class Nil[+B]() extends Node[B] {
//    override def prepend[U >: B](elem: U): ListNode[U] = ListNode(elem, this)
//  }

}
