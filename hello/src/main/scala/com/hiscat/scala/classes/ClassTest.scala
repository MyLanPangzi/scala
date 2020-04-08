package com.hiscat.scala.classes

object ClassTest {
  def main(args: Array[String]): Unit = {
    val user = new User
    println(user)
    val point = new Point(1, 2)
    point.move(2, 3)
    println(point)
    println(new Point())
    println(new Point(1))
    println(new Point(y = 1))
    val person = new Person
    person.name = "Hello"
    println(person)
    val hiscat = new Hiscat("hiscat", 18)
    println(hiscat)
    val tomcat = new Tomcat("tomcat", 10)
    println(tomcat.name)
  }

  class Tomcat(val name: String, var age: Int) {
    override def toString: String = s"$name,$age"
  }

  class Hiscat(name: String, age: Int) {
    override def toString: String = s"$name,$age"
  }

  class Person {
    private var _name = ""
    private var _age = 18

    def age: Int = _age

    def age_=(newVal: Int): Unit = {
      this._age = newVal
    }

    def name: String = _name

    def name_=(newVal: String): Unit = {
      _name = newVal
    }

    private def hello(): Unit = println("Hello")

    override def toString: String = s"$name $age"
  }

  class User

  class Point(var x: Int = 0, var y: Int = 0) {
    def move(dx: Int, dy: Int): Unit = {
      x += dx
      y += dy
    }

    override def toString: String = s"($x,$y)"
  }

}
