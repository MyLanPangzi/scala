package com.hiscat.scala.caseclass

import scala.util.Random

object CaseClassTest {
  def main(args: Array[String]): Unit = {
    println(Book("123"))
    val message1 = Message("hello", "world", "hiscat")
    val message2 = Message("hello", "world", "hiscat")
    println(message1.body)
    println(message1 == message2)
    println(message1.copy(sender = message1.recipient, recipient = "hiscat",body = "world"))
  }

  case class Message(sender: String, recipient: String, body: String)

  case class Book(isbn: String)

}
