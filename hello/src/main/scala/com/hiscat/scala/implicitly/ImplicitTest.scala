package com.hiscat.scala.implicitly

import com.hiscat.scala.implicitly.ImplicitTest.Monoid

object ImplicitTest {
  def main(args: Array[String]): Unit = {
    println(sum(List(1, 2, 3)))
    /*    implicit val stringMonoid: Monoid[String] = new Monoid[String] {
          override def add(x: String, y: String): String = x + y

          override def unit: String = "a"
        }*/
    val implicitClass = ImplicitClass("s")
    println(sum(List("a", "b", "c"))(implicitClass.stringMonoid))
    println(sum(List("a", "b", "c")))

  }

  implicit class ImplicitClass(s: String) {
    implicit val stringMonoid: Monoid[String] = new Monoid[String] {
      override def add(x: String, y: String): String = x + y

      override def unit: String = s
    }

  }

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    override def add(x: Int, y: Int): Int = x + y

    override def unit: Int = 0
  }
  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def add(x: String, y: String): String = x + y

    override def unit: String = ""
  }

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A = {
    if (xs.isEmpty) m.unit
    else m.add(xs.head, sum(xs.tail))
  }

  abstract class Monoid[A] {
    def add(x: A, y: A): A

    def unit: A

  }


}
