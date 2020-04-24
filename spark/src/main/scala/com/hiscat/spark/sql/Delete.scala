package com.hiscat.spark.sql

import java.io.File
import java.nio.file.{Files, Paths}


object Delete {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
  }
}
