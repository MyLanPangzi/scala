package com.hiscat.scala.compoundtype

object CompoundTypeTest {
  def main(args: Array[String]): Unit = {

  }

  trait Cloneable extends java.lang.Cloneable {
    override def clone(): Cloneable = super.clone().asInstanceOf[Cloneable]
  }

  trait Resetable {
    def reset(): Unit
  }

  def cloneAndReset(obj: Cloneable with Resetable): Cloneable = {
    val cloned = obj.clone()
    obj.reset()
    cloned
  }

}
