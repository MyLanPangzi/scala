package com.hiscat.scala.patternmatch

object MatchTest {
  def main(args: Array[String]): Unit = {
    val x = 10
    val y = 20
    val op = 'd'
    val r = op match {
      case '+' => x + y
      case '-' => x - y
      case '*' => x * y
      case '/' => x / y
      case _ => "illegal"
    }
    println(r)

    println(desc(5))
    println(desc("hello"))
    println(desc(true))
    println(desc('+'))
    println(desc("++"))

    println()
    println(desc2(1))
    println(desc2("hello"))
    println(desc2(List(1, 2)))
    println(desc2(List("1")))
    println(desc2(Array(1)))
    println(desc2(Array("1")))
    println(desc2(Array(1.0)))
    println(desc2(Set(1)))

    val arr = Array(
      Array(0),
      Array(1, 0),
      Array(0, 1, 0),
      Array(1, 1, 0),
      Array(1, 1, 0, 1),
      Array("hello", 90)
    )
    println()
    for (elem <- arr) {
      elem match {
        case Array(0) => println(0)
        case Array(x, y) => println(s"$x,$y")
        case Array(0, _*) => println("start 0")
        case Array(1, _*) => println(1)
        case Array(x: String, _*) => println(x.mkString(","))
        case _ => println("other")
      }
    }
    println()
    val brr = Array(List(0), List(1, 0), List(0, 0, 0), List(1, 0, 0), List(88))
    for (elem <- brr) {
      elem match {
        case List(0) => println(0)
        case List(1, 0) => println(1)
        case List(0, _*) => println("many 0")
        case _ => println("other")
      }
    }

    println()
    val crr = Array((0, 1), (1, 0), (1, 1), (1, 0, 2))
    for (elem <- crr) {
      elem match {
        case (0, _) => println("0")
        case (1, 0) => println("1")
        case (x, y) => println(s"$x,$y")
        case _ => println("other")
      }
    }
    List(1, 2, 3, 4) match {
      case first :: second :: rest => println(s"$first,$second,$rest")
    }
    val ms = Map("a" -> 1, "b" -> 0, "c" -> 2)
    for ((k, 0) <- ms) println(s"$k")
    for (("a", v) <- ms) println(s"$v")
  }

  def desc2(x: Any): String = x match {
    case i: Int => "Int"
    case s: String => "String"
    case m: List[_] => "List"
    case c: Array[Int] => "Array[Int]"
    case c: Array[String] => "Array[String]"
    case c: Array[_] => "Array[_]"
    case someThing => "someThing" + someThing
  }

  def desc(x: Any): String = x match {
    case 5 => "Five"
    case "hello" => "String hello"
    case true => "Boolean true"
    case '+' => "Char +"
    case _ => "Other"
  }

  class User(val name: String, val age: Int)

  object User {
    def apply(name: String, age: Int): User = new User(name, age)

    def unapply(arg: User): Option[(String, Int)] = if (arg == null) None else Some(arg.name, arg.age)
  }

  abstract class Expr

  case class Var(name: String) extends Expr

  case class Number(num: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

}
