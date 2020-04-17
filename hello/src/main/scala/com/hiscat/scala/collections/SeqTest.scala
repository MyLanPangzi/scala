package com.hiscat.scala.collections

object SeqTest {
  def main(args: Array[String]): Unit = {
    val xs = Seq(1, 2, 3, 4, 5, 6)
    //    Indexing and Length:
    println(xs(1))
    println(xs isDefinedAt 0)
    println(xs.length)
    println(xs.lengthCompare(6))
    println(xs.indices)

    //    Index Search:
    println(xs indexOf 2)
    println(xs lastIndexOf 5)
    println(xs indexOfSlice Seq(2, 3))
    println(xs lastIndexOfSlice Seq(4, 5, 6))
    println(xs.indexWhere(_ > 2))
    println(xs.segmentLength(_ > 1, 1))

    //    Additions
    println(-1 +: xs.prepended(0))
    println(Seq(-3, -2) ++: xs.prependedAll(Seq(-1, 0)))
    println(xs.appended(7) :+ 8)
    println(xs.appendedAll(Seq(7)) :++ Seq(8))
    println(xs.padTo(10, 0))

    //Updates
    println(xs.patch(0, Seq(0), 1))
    println(xs.updated(0, 0))
    //    println(xs(0) = 0)

    //    Sorting:
    println(Seq(3, 2, 1).sorted)
    println(xs.sortWith(_ > _))
    println(xs.sortBy(1.0 / _))

    //    Reversals:
    println(xs.reverse)
    println(xs.reverseIterator.mkString(","))

    //    Comparisons:
    //noinspection SameElementsToEquals
    println(xs.sameElements(Seq(1, 2, 3, 4, 5, 6)))
    println(xs.startsWith(Seq(1, 2, 4)))
    println(xs.endsWith(Seq(6)))
    println(xs.contains(1))
    println(xs.search(1).insertionPoint)
    println(xs.containsSlice(Seq(1, 2, 3)))
    println(xs.corresponds(Seq(7, 8, 9, 10, 11, 12))(_ < _))

    //    Multiset Operations:
    println(xs intersect Seq(1, 3, 5, 7))
    println(xs diff Seq(1, 3, 5, 7))
    println(Seq(1, 2, 3, 4, 1, 2, 3).distinct)
    println(xs.distinctBy(_ % 2 == 0))
  }
}
