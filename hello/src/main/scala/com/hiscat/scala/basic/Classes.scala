package com.hiscat.scala.basic

object Classes {
  def main(args: Array[String]): Unit = {
    new Greeter("hello ", " world").greet("scala")
  }
}

class Greeter(prefix: String, suffix: String) {
  def greet(name: String): Unit = {
    println(prefix + name + suffix)
  }
}
