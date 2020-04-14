package com.hiscat.scala.oop

import scala.collection.mutable.ArrayBuffer

object OOPTest {
  def main(args: Array[String]): Unit = {
    val p1 = new Pizza(
      MediumCrustSize,
      ThickCrustType,
      ArrayBuffer(Cheese)
    )
    val p2 = new Pizza(
      LargeCrustSize,
      ThinCrustType,
      ArrayBuffer(Cheese, Pepperoni, Sausage)
    )
    val address = new Address(
      "123 Main Street",
      "Apt.1 ",
      "Talkeetna",
      "Alaska",
      "99676"
    )
    val customer = new Customer(
      "Ali",
      "907-555-1212",
      address
    )
    val o = new Order(
      ArrayBuffer(p1, p2),
      customer
    )
    o.printOrder()
  }
}

class Pizza(
             var crustSize: CrustSize,
             var crustType: CrustType,
             var toppings: ArrayBuffer[Topping]
           ) {

  def addTopping(t: Topping): Unit = toppings += t

  def removeTopping(t: Topping): Unit = toppings -= t

  def removeAllTopping(): Unit = toppings.clear()

  def getPrice(
                toppingPrices: Map[Topping, Int],
                crustSizePrices: Map[CrustSize, Int],
                crustTypePrices: Map[CrustType, Int]
              ): Int = ???

  override def toString = s"Pizza($crustSize, $crustType, $toppings)"
}

class Order(
             var pizzas: ArrayBuffer[Pizza],
             var customer: Customer
           ) {

  def printOrder(): Unit = println(toString)

  def addPizza(p: Pizza): Unit = pizzas += p

  def removePizza(p: Pizza): Unit = pizzas -= p

  def getBasePrice(): Int = ???

  def getTaxes(): Int = ???

  def getTotalPrice(): Int = ???

  override def toString = s"Order($pizzas, $customer)"
}

class Customer(
                var name: String,
                var phone: String,
                var address: Address
              ){


  override def toString = s"Customer($name, $phone, $address)"
}

class Address(
               var street1: String,
               var street2: String,
               var city: String,
               var state: String,
               var zipCode: String
             ){


  override def toString = s"Address($street1, $street2, $city, $state, $zipCode)"
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

