package com.hiscat.scala.caseclass

import scala.util.Random

object CaseClassTest {
  def main(args: Array[String]): Unit = {
    println(Book("123"))
    val message1 = Message("hello", "world", "hiscat")
    val message2 = Message("hello", "world", "hiscat")
    println(message1.body)
    println(message1 == message2)
    println(message1.copy(sender = message1.recipient, recipient = "hiscat", body = "world"))
    val student = Student("A", 1)
    val teacher = Teacher("B", "b")
    println(getPrintableString(student))
    println(getPrintableString(teacher))
    val cubs1908 = BaseballTeam("Chicago Cubs", 1908)
    println(cubs1908)
    println(cubs1908.copy(lastWorldSeriesWin = 2016))
  }

  case class BaseballTeam(name: String, lastWorldSeriesWin: Int)

  case class Message(sender: String, recipient: String, body: String)

  case class Book(isbn: String)

  //  case class Person(name: String, relation: String)
  trait Person {
    def name: String
  }

  case class Student(name: String, year: Int) extends Person

  case class Teacher(name: String, specialty: String) extends Person

  def getPrintableString(p: Person): String = p match {
    case Student(name, year) => s"$name is a student in Year $year"
    case Teacher(name, specialty) => s"$name teaches $specialty"
    case _ => ""
  }
}
