package com.hiscat.scala

package object packageobject {
  val planted = List(Apple, Plum, Banana)

  def showFruit(fruit: Fruit): Unit = {
    println(s"${fruit.name}s are ${fruit.color}")
  }
}
