package com.hiscat.scala.packageobject

object PackageObjectTest {
  def main(args: Array[String]): Unit = {
    for (fruit <- planted) {
      showFruit(fruit)
    }

    def f: Int => ((Int, Int) => Int) => Int => Int = x => f => y => f(x, y)

    println(f(10)(_ + _)(20))
  }
}
