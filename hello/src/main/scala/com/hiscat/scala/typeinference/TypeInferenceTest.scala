package com.hiscat.scala.typeinference

object TypeInferenceTest {
  def main(args: Array[String]): Unit = {
    println(fac(3))
    println(f(10)(20))

    def f: Int => Int => Int = x => y => x * y

    def m: Int => Int => ((Int, Int) => Int) => Int = x => y => f => f(x, y)

    println(f(10)(20))
    println(m(10)(20)(_ * _))
  }

  def m(x: Int): Int => ((Int, Int) => Int) => Int = {
    def m2(y: Int): ((Int, Int) => Int) => Int = {
      def m3(f: (Int, Int) => Int): Int = {
        f(x, y)
      }

      m3
    }

    m2
  }

  def f(x: Int): Int => Int = y => x * y

  def fac(n: Int): Int = if (n == 0) 1 else n * fac(n - 1)

}
