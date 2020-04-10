package com.hiscat.scala.extractor

import scala.util.Random

object ExtractorTest {
  def main(args: Array[String]): Unit = {
    val hello = CustomerID("Hello")
    hello match {
      case CustomerID(name) => println(name)
      case _ => println("Could not extract a ID")
    }
    val nico = CustomerID("Nico")
    val CustomerID(name) = nico
    println(name)
  }

  object CustomerID {
    def apply(name: String) = s"$name--${Random.nextLong}"

    def unapply(id: String): Option[String] = {
      //      Option("hello")
      val strings = id.split("--")
      if (strings.tail.nonEmpty) Some(strings.head) else None
    }
  }

}
