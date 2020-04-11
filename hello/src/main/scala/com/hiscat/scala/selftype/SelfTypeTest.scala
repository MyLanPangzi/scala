package com.hiscat.scala.selftype

object SelfTypeTest {
  def main(args: Array[String]): Unit = {
    val hello = new VerifiedTweeter("Hello")
    hello.tweet("world")
  }

  trait User {
    def username: String
  }

  trait Tweeter {
    this: User =>
    def tweet(text: String): Unit = println(s"$username: $text")
  }

  class VerifiedTweeter(val username_ : String) extends Tweeter with User {
    override def username: String = s"real $username_"
  }

}
