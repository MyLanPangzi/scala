package com.hiscat.scala.collections

object CommonSeqMethodsTest {
  def main(args: Array[String]): Unit = {
    val nums = (1 to 10).toList
    val names = List("joel", "ed", "chris", "maurice")
    println(nums)
    println(nums.map(_ * 2))
    println(names.map(_.capitalize))

    println(nums.filter(_ < 5))
    println(nums.filter(_ % 2 == 0))
    println(names.filter(_.length <= 4))

    names.foreach(println)
    nums.filter(_ < 4).foreach(println)

    println(nums.head)
    println(names.head)
    println("foo".head)
    //    println(Nil.head)
    println(Nil.headOption)
    println(nums.tail)
    println(names.tail)
    println("foo".tail)
    //    println(Nil.tail)
    println(nums.take(1))
    println(nums.take(2))
    println(names.take(1))
    println(names.take(2))
    println(nums.takeWhile(_ < 5))
    println(names.takeWhile(_.length < 5))
    println(nums.drop(1))
    println(nums.drop(5))
    println(names.drop(1))
    println(names.drop(2))
    println(nums.dropWhile(_ < 5))
    println(names.dropWhile(_ != "chris"))

    val a = List(1, 2, 3, 4)
    val sum = a.reduce { (acc, i) =>
      println(s"i:$i,acc:$acc")
      i + acc
    }
    println(sum)
    println(a.product)
  }
}
