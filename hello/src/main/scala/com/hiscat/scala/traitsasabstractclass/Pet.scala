package com.hiscat.scala.traitsasabstractclass

trait Pet {
  def speak(): Unit = println("Yo")

  def comeToMaster(): Unit
}
