package com.hiscat.scala.traitsasabstractclass

object MultipleTraitsTest {
  def main(args: Array[String]): Unit = {
    val d = new Dog("hello")
    d.speak()
    d.startRunning()
    d.stopRunning()

    val cat = new Cat
    cat.speak()
    cat.startRunning()
    cat.stopRunning()
  }

  class Dog(name: String) extends Speaker with TailWagger with Runner {
    override def speak(): String = "Woof!"
  }

  class Cat extends Speaker with TailWagger with Runner {
    override def speak(): String = "Meow"

    override def startRunning(): Unit = println("Yeah ... I don't run")

    override def stopRunning(): Unit = println("No need to stop")
  }

}
