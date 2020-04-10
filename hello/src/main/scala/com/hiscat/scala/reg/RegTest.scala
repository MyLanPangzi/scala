package com.hiscat.scala.reg

import scala.util.matching.Regex

object RegTest {
  def main(args: Array[String]): Unit = {
    val reg = "[0-9]".r
    reg.findFirstMatchIn("a") match {
      case Some(_) => println("OK")
      case None => println("Not OK")
    }
    val keyValPattern: Regex = "([0-9a-zA-Z- ]+): ([0-9a-zA-Z-#()/. ]+)".r

    val input: String =
      """background-color: #A03300;
        |background-image: url(img/header100.png);
        |background-position: top center;
        |background-repeat: repeat-x;
        |background-size: 2160px 108px;
        |margin: 0;
        |height: 108px;
        |width: 100%;""".stripMargin
    for (p <- keyValPattern.findAllMatchIn(input)) {
      println(s"k:${p.group(1)} v:${p.group(2)}")
    }
  }

}
