package com.hiscat.scala.packageobject

object PackageObjectTest {
  def main(args: Array[String]): Unit = {
    for (fruit <- planted) {
      showFruit(fruit)
    }
  }
}
