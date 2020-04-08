package com.hiscat.scala.traits

import scala.collection.mutable.ArrayBuffer

object TraitTest {
  def main(args: Array[String]): Unit = {
    val iterator = new IntIterator(10)
    println(iterator.next)
    println(iterator.next)
    val animals = ArrayBuffer.empty[Pet]
    animals.append(new Dog("Harry"))
    animals.append(new Cat("Sally"))
    animals.foreach(pet=> println(pet.name))
  }

  trait Pet {
    val name: String
  }

  class Cat(val name: String) extends Pet

  class Dog(val name: String) extends Pet

  trait HairColor

  trait Iterator[A] {
    def hasNext: Boolean

    def next: A
  }

  class IntIterator(to: Int) extends Iterator[Int] {
    private var current = 0

    override def hasNext: Boolean = current < to

    override def next: Int = {
      if (hasNext) {
        val t = current
        current += 1
        t
      } else 0
    }
  }

}
