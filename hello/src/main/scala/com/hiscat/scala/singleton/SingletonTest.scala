package com.hiscat.scala.singleton

import scala.math._

object SingletonTest {
  def main(args: Array[String]): Unit = {
    println(Circle(5.0).area)

    val email = Email.fromString("hiscat@qq.com")
    email match {
      case Some(email) => println(
        s"""Registered an email
           |Username: ${email.username}
           |Domain name: ${email.domainName}
     """.stripMargin)
      case None => println("Error: could not parse email")
    }
    println("hello")


  }

  def myprint(s: String): String = {
    s
  }

  object Box

  object Logger {
    def info(message: String): Unit = {
      println(s"INFO:$message")
    }
  }

}

case class Circle(radius: Double) {

  import Circle._

  def area: Double = calcArea(radius)
}

object Circle {
  private def calcArea(radius: Double): Double = Pi * pow(radius, 2.0)
}

class Email(val username: String, val domainName: String)

object Email {
  def fromString(emailString: String): Option[Email] = {
    emailString.split('@') match {
      case Array(a, b) => Some(new Email(a, b))
      case _ => None
    }
  }
}