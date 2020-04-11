package com.hiscat.scala.operator

object OperatorTest {
  def main(args: Array[String]): Unit = {
    val v1 = Vec(1, 2)
    val v2 = Vec(2, 2)
    val v3 = v1 + v2
    println(v3)
  }

  case class MyBool(x: Boolean) {
    def and(that: MyBool): MyBool = if (x) that else this

    def or(that: MyBool): MyBool = if (x) this else that

    def negate: MyBool = MyBool(!x)

    def not(x: MyBool): MyBool = x.negate

    def xor(x: MyBool, y: MyBool): MyBool = (x or y) and not(x and y)
  }

  case class Vec(x: Double, y: Double) {
    def +(that: Vec): Vec = Vec(this.x + that.x, this.y + that.y)
  }

}
