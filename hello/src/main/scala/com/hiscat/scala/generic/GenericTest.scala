package com.hiscat.scala.generic

object GenericTest {
  def main(args: Array[String]): Unit = {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    println(stack.pop())
    println(stack.pop())
    val fruits = new Stack[Fruit]
    fruits.push(new Apple)
    fruits.push(new Banana)
  }

  class Fruit

  class Apple extends Fruit

  class Banana extends Fruit

  class Stack[A] {
    private var elements: List[A] = Nil

    def push(x: A) {
      elements = x :: elements
    }

    def peek: A = elements.head

    def pop(): A = {
      val currentTop = peek
      elements = elements.tail
      currentTop
    }
  }

}
