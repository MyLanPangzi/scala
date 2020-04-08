package com.hiscat.scala.io

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}

import scala.io.Source

object IOTest {
  //noinspection SourceNotClosed
  def main(args: Array[String]): Unit = {
    //    Source.fromFile("E:\\github\\scala\\hello\\src\\main\\scala\\com\\hiscat\\scala\\io\\IOTest.scala").foreach(line => print(line))
    val source = Source.fromFile("input/test.txt")
    source.foreach(line => print(line))
    println()
    val writer = new PrintWriter(new File("output/test.txt"))
    writer.write("Hello Scala")
    writer.close()
  }

}
