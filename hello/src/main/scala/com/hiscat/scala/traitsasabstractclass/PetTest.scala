package com.hiscat.scala.traitsasabstractclass

object PetTest {
  def main(args: Array[String]): Unit = {
    val d = new Dog("zeus")
    d.speak()
    d.comeToMaster()

    val cat = new Cat
    cat.speak()
    cat.comeToMaster()
  }

  class Dog(name: String) extends Pet {
    override def comeToMaster(): Unit = println("Woo-hoo,I'm coming!")
  }
  class Cat extends Pet{
    override def speak(): Unit = println("meow")

    override def comeToMaster(): Unit = println("That's not gonna happen.")
  }

}
