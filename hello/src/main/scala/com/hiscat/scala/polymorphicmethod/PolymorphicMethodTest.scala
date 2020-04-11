package com.hiscat.scala.polymorphicmethod

object PolymorphicMethodTest {
  def main(args: Array[String]): Unit = {
    println(listOfDuplicates(3, 4))
    println(listOfDuplicates("La", 4))
  }

  def listOfDuplicates[A](x: A, length: Int): List[A] = {
    if (length < 1) {
      Nil
    } else
      x :: listOfDuplicates(x, length - 1)
  }
}
