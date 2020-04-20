package com.hiscat.scala.implicitly

import com.hiscat.scala.implicitly.ImplicitTest2.User

object ImplicitTest2 {

  def main(args: Array[String]): Unit = {
    implicit def f: Int => String = i => i.toString

    implicit val i: Int = 10
    val s: String = i
    println(s)

    def m(implicit i: Int): Unit = println(i)

    m

    implicit def t(user: User): Close = {
      new User with Close
    }

    //当前作用域 --》伴生对象/类 --》包对象 --> import
    val user = new User
    user.close()
    user.open()
    user.init()
    import com.hiscat.scala.implicitly.Test.Hello
    user.hello()

  }

  implicit class Open(user: User) {
    def open(): Unit = println("open")
  }

  trait Close {
    def close(): Unit = println("close")
  }

  class User {

  }


}

object Test {

  implicit class Hello(user: User) {
    def hello(): Unit = println("hello")
  }

}
