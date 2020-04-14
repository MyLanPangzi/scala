package com.hiscat.scala.classes

class Stock(var symbol: String, var price: BigDecimal){

  override def toString = s"Stock(symbol=$symbol, price=$price)"
}
