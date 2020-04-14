package com.hiscat.scala.companion

object CompanionTest {
  def main(args: Array[String]): Unit = {
    //    println(Person.apply("Hello").name)
    //    println(Person("hello").name)
    //    println(Person)
    println(Person(Some("Fred")))
    println(Person(None))
    println(Person(Some("Wilma"), Some(33)))
    println(Person(Some("Wilma"), None))
  }

  class Person {
    var name: Option[String] = None
    var age: Option[Int] = None

    override def toString = s"Person($name, $age)"
  }

  object Person {
    def apply(name: Option[String]): Person = {
      val person = new Person
      person.name = name
      person
    }

    def apply(name: Option[String], age: Option[Int]): Person = {
      val person = new Person
      person.name = name
      person.age = age
      person
    }

    //  def apply(name: String): Person = {
    //    val p = new Person()
    //    p.name = name
    //    p
    //  }
  }
}

