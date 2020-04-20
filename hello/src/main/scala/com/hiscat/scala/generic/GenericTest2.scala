package com.hiscat.scala.generic


object GenericTest2 {
  def main(args: Array[String]): Unit = {
    val t: Test1[P] = new Test1[P]
    val t1: Test2[P] = new Test2[P]
    val t2: Test2[P] = new Test2[S]
    val list = List[P](new S)
    new S :: list
    new P :: list


  }

  class Test1[T]

  class Test2[+P]

  class Test3[-P]

  class P

  class S extends P

}
