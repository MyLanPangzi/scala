package com.hiscat.scala.variance

import scala.util.control.Breaks

object CovarianceTest {
  def main(args: Array[String]): Unit = {
    val cats = List(Cat("Whiskers"), Cat("Tom"))
    val dgos = List(Dog("Fido"), Dog("Rex"))
    printNames(cats)
    printNames(dgos)

    val cat = Cat("Boots")

    def printMyCat(printer: Printer[Cat]): Unit = {
      printer.print(cat)
    }

    val catPrinter = new CatPrinter
    val animalPrinter = new AnimalPrinter
    printMyCat(catPrinter)
    printMyCat(animalPrinter)

    val catContainer = new Container(Cat("Felix"))
//    val animalContainer:Container[Animal]=catContainer

  }

  def printNames(animals: List[Animal]): Unit = {
    animals.foreach(a => println(a.name))
  }

  class Container[A](value: A) {
    private var _value: A = value

    def getValue: A = _value

    def setValue(value: A): Unit = {
      _value = value
    }
  }

  abstract class Printer[-A] {
    def print(value: A): Unit
  }

  class AnimalPrinter extends Printer[Animal] {
    override def print(animal: Animal): Unit =
      println(s"The animal's name is: ${animal.name}")
  }

  class CatPrinter extends Printer[Cat] {
    override def print(cat: Cat): Unit =
      println(s"The cat's name is: ${cat.name}")
  }

  abstract class Animal {
    def name: String
  }

  case class Cat(name: String) extends Animal

  case class Dog(name: String) extends Animal

}
