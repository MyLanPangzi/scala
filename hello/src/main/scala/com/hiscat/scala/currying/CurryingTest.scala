package com.hiscat.scala.currying

object CurryingTest {
  def main(args: Array[String]): Unit = {
    val nums = List(1, 2, 3, 4)
    println(nums.foldLeft(0)(_ + _))
    println(nums.sum)
    //    println(foldLeft1[Int, Int](nums, 0, _ + _))
    //    println(foldLeft1(nums, 0, (a: Int, b: Int) => a + b))
    val func = nums.foldLeft(List[Int]()) _
    println(func((a, b) => a :+ b + b))
    //    val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    //    val numberFunc = numbers.foldLeft(List[Int]()) _
    //
    //    val squares = numberFunc((xs, x) => xs :+ x * x)
    //    println(squares) // List(1, 4, 9, 16, 25, 36, 49, 64, 81, 100)
    //
    //    val cubes = numberFunc((xs, x) => xs :+ x * x * x)
    //    println(cubes) // List(1, 8, 27, 64, 125, 216, 343, 512, 729, 1000)
  }

  def foldLeft1[A, B](as: List[A], b0: B, op: (B, A) => B) = ???

  def foldLeft2[A, B](as: List[A], b0: B)(op: (B, A) => B) = ???
}
