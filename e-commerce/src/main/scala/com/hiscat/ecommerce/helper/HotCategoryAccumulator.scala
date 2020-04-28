package com.hiscat.ecommerce.helper

import com.hiscat.ecommerce.bean.HotCategory
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

/**
 * 热门品类累加器
 * String : 输入的数据
 * Map[String, HotCategory] ： 表示品类对应的点击数量
 */
class HotCategoryAccumulator extends AccumulatorV2[String, mutable.Map[String, HotCategory]] {
  private var hotCategoryMap = mutable.Map[String, HotCategory]()

  override def isZero: Boolean = hotCategoryMap.isEmpty

  override def copy(): AccumulatorV2[String, mutable.Map[String, HotCategory]] = {
    val accumulator = new HotCategoryAccumulator
    accumulator.hotCategoryMap = hotCategoryMap.clone()
    accumulator
  }

  override def reset(): Unit = hotCategoryMap.clear()

  override def add(v: String): Unit = {
    val event = v.split("_")
    if (event(6) != "-1") {
      hotCategoryMap.getOrElseUpdate(event(6), HotCategory(event(6), 0, 0, 0)).clickCount += 1
    } else if (event(8) != "null") {
      event(8).split(",").foreach(c => {
        hotCategoryMap.getOrElseUpdate(c, HotCategory(c, 0, 0, 0)).orderCount += 1
      })
    } else if (event(10) != "null") {
      event(10).split(",").foreach(c => {
        hotCategoryMap.getOrElseUpdate(c, HotCategory(c, 0, 0, 0)).payCount += 1
      })
    }

  }

  override def merge(other: AccumulatorV2[String, mutable.Map[String, HotCategory]]): Unit = {
    other.value.foreach {
      case (c, category) =>
        val hot = hotCategoryMap.getOrElseUpdate(c, HotCategory(c, 0, 0, 0))
        hot.clickCount += category.clickCount
        hot.orderCount += category.orderCount
        hot.payCount += category.payCount
    }
  }

  override def value: mutable.Map[String, HotCategory] = hotCategoryMap
}
