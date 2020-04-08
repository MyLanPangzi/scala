package com.hiscat.scala.basic

object Traits {
  def main(args: Array[String]): Unit = {
    (new DefaultHello).hello("World")
    new CustomizableHello("How are you, ", "?").hello("Scala")
  }
}

trait Greeter {
  def greet(name: String): Unit
}

trait Hello {
  def hello(name: String): Unit = {
    println("Hello " + name)
  }
}

class DefaultHello extends Hello

class CustomizableHello(prefix: String, postfix: String) extends Hello {
  override def hello(name: String): Unit = {
    println(prefix + name + postfix)
  }
}