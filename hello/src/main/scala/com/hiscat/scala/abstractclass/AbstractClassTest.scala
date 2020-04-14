package com.hiscat.scala.abstractclass

object AbstractClassTest {
  def main(args: Array[String]): Unit = {
    val d = new Dog("Rover")
    d.speak()
    d.comeToMaster()
  }

  class Dog(name: String) extends Pet(name) {

//    override def speak(): Unit = println("Woof")

    override def comeToMaster(): Unit = println("Here I come!")
  }

}
