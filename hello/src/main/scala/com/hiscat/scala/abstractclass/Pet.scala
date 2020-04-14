package com.hiscat.scala.abstractclass

abstract class Pet(name: String) {
  def speak(): Unit = println(s"My name is $name")

  def comeToMaster(): Unit
}
