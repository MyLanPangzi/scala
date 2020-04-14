package com.hiscat.scala.traits

object TailWaggerTest {
  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog.startTail()
    dog.stopTail()
    dog.startRunning()
    dog.stopRunning()
    println(dog.speak())
  }

  class Dog extends TailWagger with Speaker with Runner {
    override def startTail(): Unit = println("tail is wagging")

    override def stopTail(): Unit = println("tail is stopped")

    override def speak(): String = "Woof!"

    override def startRunning(): Unit = println("I'm running")

    override def stopRunning(): Unit = println("Stopped running")
  }

}
