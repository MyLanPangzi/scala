package com.hiscat.scala.uppertypebound

object UpperTypeBoundTest {
  def main(args: Array[String]): Unit = {
    new PetContainer[Dog](new Dog)
    new PetContainer[Cat](new Cat)
//    new PetContainer[Lion](new Lion)
  }

  abstract class Animal {
    def name: String
  }

  abstract class Pet extends Animal

  class Cat extends Pet {
    override def name: String = "Cat"
  }

  class Dog extends Pet {
    override def name: String = "Dog"
  }

  class Lion extends Animal {
    override def name: String = "Lion"
  }

  class PetContainer[P <: Pet](p: P) {
    def pet: P = p;
  }

}
