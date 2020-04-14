package com.hiscat.scala.companion

object UnapplyTest {
  def main(args: Array[String]): Unit = {
    val lori = new Person("Lori", 29)
    println(Person.unapply(lori))
    val (name, age) = Person.unapply(lori)
    println(s"$name,$age")
  }


  class Person(var name: String, var age: Int)

  object Person {
    //    def unapply(person: Person): String = s"${person.name},${person.age}"
    def unapply(person: Person): (String, Int) = (person.name, person.age)
  }

}
