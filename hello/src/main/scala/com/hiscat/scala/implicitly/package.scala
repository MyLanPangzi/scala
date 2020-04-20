package com.hiscat.scala

import com.hiscat.scala.implicitly.ImplicitTest2.User

package object implicitly {

  implicit class Init(user: User) {
    def init(): Unit = println("init")
  }

}
