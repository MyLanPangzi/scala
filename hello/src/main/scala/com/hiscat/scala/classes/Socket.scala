package com.hiscat.scala.classes

class Socket(val timeout: Int = 2000, val linger: Int = 3000) {

  override def toString = s"Socket(timeout=$timeout, linger=$linger)"
}
