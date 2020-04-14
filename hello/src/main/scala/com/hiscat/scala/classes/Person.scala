package com.hiscat.scala.classes

class Person(var firstName: String, var lastName: String) {
  println("constructor")
  var age = 0
  private val HOME = System.getProperty("user.home")

  override def toString: String = s"$firstName.$lastName $age "

  def printHome(): Unit = println(s"HOME=$HOME")

  def printFullName(): Unit = println(this)

  printHome()
  printFullName()
  println("construct end")
}
