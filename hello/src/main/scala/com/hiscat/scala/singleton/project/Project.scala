package com.hiscat.scala.singleton.project

import com.hiscat.scala.singleton.SingletonTest.Logger.info

class Project(name: String, daysToComplete: Int)

class Test {
  val project1 = new Project("TPS Reports", 1)
  val project2 = new Project("Web redesign", 5)
  info("Created projects")
}
