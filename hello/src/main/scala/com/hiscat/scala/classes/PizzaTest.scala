package com.hiscat.scala.classes

object PizzaTest {

  class User {
    var name: String = _
  }

  def main(args: Array[String]): Unit = {
    val p = new Pizza
    p.addTopping(Cheese)
    p.addTopping(Pepperoni)
    println(p)
    p.removeTopping(Cheese)
    println(p)
    p.removeAllTopping()
    println(p)
    //    val p1 = new Pizza(DefaultCrustSize, DefaultCrustType)
    //    val p2 = new Pizza(DefaultCrustSize)
    //    val p3 = new Pizza()
    //    val p4 = new Pizza()
    //    println(p1)
    //    println(p2)
    //    println(p3)
    //    println(p4)
  }
}
