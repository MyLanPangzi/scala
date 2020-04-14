package com.hiscat.scala.classes

import scala.collection.mutable.ArrayBuffer

class Pizza(
             var crustSize: CrustSize = MediumCrustSize,
             var crustType: CrustType = RegularCrustType
           ) {


  val toppings: ArrayBuffer[Topping] = ArrayBuffer[Topping]()


  def addTopping(t: Topping): Unit = toppings += t

  def removeTopping(t: Topping): Unit = toppings -= t

  def removeAllTopping(): Unit = toppings.clear()


  override def toString: String = {
    s"""
       |Crust Size: $crustSize
       |Crust Type: $crustType
       |Toppings:   $toppings
        """.stripMargin
  }
}

sealed trait Topping

case object Cheese extends Topping

case object Pepperoni extends Topping

case object Sausage extends Topping

case object Mushrooms extends Topping

case object Onions extends Topping

sealed trait CrustSize

case object SmallCrustSize extends CrustSize

case object MediumCrustSize extends CrustSize

case object LargeCrustSize extends CrustSize

sealed trait CrustType

case object RegularCrustType extends CrustType

case object ThinCrustType extends CrustType

case object ThickCrustType extends CrustType