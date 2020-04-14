package com.hiscat.scala.traitsasabstractclass

trait Runner {
  def startRunning(): Unit = println("I'm running")

  def stopRunning(): Unit = println("Stopped running")
}
