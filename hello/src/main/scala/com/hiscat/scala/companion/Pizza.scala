package com.hiscat.scala.companion

class Pizza {
  def printFilename(): Unit = {
    println(Pizza.HiddenFilename)
  }
}

object Pizza {
  private val HiddenFilename = "/tmp/foo.bar"
}