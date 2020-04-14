package com.hiscat.scala.traitsasabstractclass

object MixinsTest {
  def main(args: Array[String]): Unit = {
    val dog = new Dog("Fido") with TailWagger with Runner
    dog.startTail()
    dog.startRunning()
  }

  class Dog(name: String)

}
