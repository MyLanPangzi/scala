package com.hiscat.scala.forcomprehension

object ForComprehensionTest {

  case class User(name: String, age: Int)

  def main(args: Array[String]): Unit = {
    val users = List(User("Travis", 28), User("Kelly", 33), User("Jennifer", 44), User("Dennis", 23))
    val names = for (user <- users if user.age >= 20 && user.age < 30) yield user.name
    names.foreach(println(_))
    List(User("Travis", 28), User("Kelly", 33),
      User("Jennifer", 44), User("Dennis", 23))
      .filter(u => u.age >= 20 && u.age <= 30)
      .map(_.name)
      .foreach(println(_))
    foo(10, 10) foreach {
      case (i, j) => println(s"$i,$j")
    }
    println()
    bar(10, 10)
  }

  def bar(n: Int, v: Int): Unit =
    for (i <- 0 until n; j <- 0 until n if j + i == v) {
      println(s"$i,$j")
    }

  def foo(n: Int, v: Int): Seq[(Int, Int)] =
    for (i <- 0 until n; j <- 0 until n if i + j == v)
      yield (i, j)
}
