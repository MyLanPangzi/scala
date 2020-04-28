package com.hiscat.ecommerce.service

import com.hiscat.ecommerce.bean.HotCategory
import com.hiscat.ecommerce.common.TService
import com.hiscat.ecommerce.dao.HotCategoryAnalysisTop10Dao
import com.hiscat.ecommerce.helper.HotCategoryAccumulator
import com.hiscat.ecommerce.util.ProjectUtil
import org.apache.spark.rdd.RDD

import scala.collection.{immutable, mutable}

/**
 * 热门品类Top10业务对象
 */
class HotCategoryAnalysisTop10Service extends TService {
  private val hotCategoryAnalysisTop10Dao = new HotCategoryAnalysisTop10Dao

  /**
   * 数据分析5
   *
   * 不使用shuffle也可以聚合数据,使用累加器实现数据聚合
   */
  def analysis5(): immutable.Seq[HotCategory] = {
    val dataRDD: RDD[String] = hotCategoryAnalysisTop10Dao.readFile("input/user_visit_action.txt")
    val acc = new HotCategoryAccumulator
    dataRDD.sparkContext.register(acc)
    dataRDD.foreach(acc.add)
    acc.value.values.toList.sortBy(c => (c.clickCount, c.orderCount, c.payCount)).reverse.take(10)
  }

  /**
   * 数据分析4
   */
  def analysis4(): Array[HotCategory] = hotCategoryAnalysisTop10Dao
    .readFile("input/user_visit_action.txt")
    .flatMap(
      data => {
        val datas = data.split("_")
        if (datas(6) != "-1") {
          List((datas(6), HotCategory(datas(6), 1, 0, 0)))
        } else if (datas(8) != "null") {
          datas(8).split(",").map(id => (id, HotCategory(id, 0, 1, 0)))
        } else if (datas(10) != "null") {
          datas(10).split(",").map(id => (id, HotCategory(id, 0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    .reduceByKey(
      (c1, c2) => {
        c1.clickCount = c1.clickCount + c2.clickCount
        c1.orderCount = c1.clickCount + c2.orderCount
        c1.payCount = c1.payCount + c2.payCount
        c1
      }
    )
    .map(_._2)
    .sortBy(
      data => (data.clickCount, data.orderCount, data.payCount),
      ascending = false
    )
    .take(10)

  /**
   * 数据分析3
   *
   * 将数据封装为样例类，操作方便
   */
  def analysis3(): Array[HotCategory] = hotCategoryAnalysisTop10Dao
    .readFile("input/user_visit_action.txt")
    .flatMap(
      data => {
        val datas = data.split("_")
        if (datas(6) != "-1") {
          List(HotCategory(datas(6), 1, 0, 0))
        } else if (datas(8) != "null") {
          datas(8).split(",").map(HotCategory(_, 0, 1, 0)
          )
        } else if (datas(10) != "null") {
          datas(10).split(",").map(HotCategory(_, 0, 0, 1))
        } else {
          Nil
        }
      }
    )
    .groupBy(_.id)
    .mapValues(
      iter => {
        iter.reduce(
          (c1, c2) => {
            c1.clickCount = c1.clickCount + c2.clickCount
            c1.orderCount = c1.orderCount + c2.orderCount
            c1.payCount = c1.payCount + c2.payCount
            c1
          }
        )
      }
    )
    .map(_._2)
    .collect()
    .sortWith(
      (left, right) => {
        if (left.clickCount > right.clickCount) {
          true
        } else if (left.clickCount == right.clickCount) {
          if (left.orderCount > right.orderCount) {
            true
          } else if (left.orderCount == right.orderCount) {
            left.payCount > right.payCount
          } else {
            false
          }
        } else {
          false
        }
      }
    )
    .take(10)

  /**
   * 数据分析2
   * // （xxx, zs, 鞋，click）
   * // （鞋，click, 1）=> (鞋，click, 1, order,0, pay, 0)
   * // （鞋，click, 1）=> (鞋，click, 1, order,0, pay, 0)
   * // （鞋，click, 1）=> (鞋，click, 1, order,0, pay, 0)
   * *
   */
  //  /(鞋
  //  ， click
  //  , 11
  //  ) => (鞋
  //  ， click
  //  , 11
  //  , order
  //  , 0
  //  , pay
  //  , 0
  //  )
  //  *//(鞋
  //  ， order
  //  , 12
  //  ) => (鞋
  //  ， click
  //  , 0
  //  , order
  //  , 12
  //  , pay
  //  , 0
  //  )
  //  *//(鞋
  //  ， pay
  //  , 17
  //  ) => (鞋
  //  ， click
  //  , 0
  //  , order
  //  , 0
  //  , pay
  //  , 17
  //  )
  //  **
  //  *//
  //  （ 鞋
  //  ， 11
  //  ， 12
  //  ， 17
  //  ）
  //  */
  def analysis2(): Array[(String, (Int, Int, Int))] = {

    hotCategoryAnalysisTop10Dao
      .readFile("input/user_visit_action.txt")
      .flatMap(
        data => {
          val datas = data.split("_")
          if (datas(6) != "-1") {
            List((datas(6), (1, 0, 0)))
          } else if (datas(8) != "null") {
            datas(8).split(",").map((_, (0, 1, 0)))
          } else if (datas(10) != "null") {
            datas(10).split(",").map((_, (0, 0, 1)))
          } else {
            Nil
          }
        }
      )
      .reduceByKey(
        (c1, c2) => {
          (c1._1 + c2._1, c1._2 + c2._2, c1._3 + c2._3)
        }
      )
      .sortBy(_._2, ascending = false)
      .take(10)
  }

  /**
   * 数据分析1
   */
  override def analysis(): Array[(String, (Int, Int, Int))] = {

    // TODO 将原始数据进行转换，形成对象，方便使用
    // TODO 品类点击统计
    val categoryClickRDD: RDD[(String, Int)] = hotCategoryAnalysisTop10Dao.readFile("input/user_visit_action.txt").cache().map(
      data => {
        val datas = data.split("_")
        // （品类，点击总数）
        (datas(6), 1)
      }
    )
    // (品类， 1)
    val categoryClickFilterRDD = categoryClickRDD.filter(_._1 != "-1")
    val categoryClickReduceRDD: RDD[(String, Int)] = categoryClickFilterRDD.reduceByKey(_ + _)

    // TODO 品类下单统计
    val categoryOrderRDD: RDD[String] = hotCategoryAnalysisTop10Dao.readFile("input/user_visit_action.txt").cache().map(
      data => {
        val datas = data.split("_")
        // （品类，下单总数）
        datas(8)
      }
    )

    // 1,2,3,4
    val categoryOrderFilterRDD = categoryOrderRDD.filter(_ != "null")

    // 1 - 2 - 3 - 4
    // (1,1),(2,1),(3,1)(4,1)
    val categoryOrdersRDD: RDD[(String, Int)] = categoryOrderFilterRDD.flatMap(
      data => {
        val ids = data.split(",")
        ids.map((_, 1))
      }
    )
    //（品类，下单总数）
    val categoryOrderReduceRDD: RDD[(String, Int)] = categoryOrdersRDD.reduceByKey(_ + _)

    //（品类，支付总数）
    val categoryPayReduceRDD: RDD[(String, Int)] = hotCategoryAnalysisTop10Dao.readFile("input/user_visit_action.txt")
      .cache().map(
      data => {
        val datas = data.split("_")
        // （品类，支付总数）
        datas(10)
      }
    ).filter(_ != "null").flatMap(
      data => {
        val ids = data.split(",")
        ids.map((_, 1))
      }
    ).reduceByKey(_ + _)

    // TODO 将之前分别的获取的RDD进行合并

    // (鞋，click, 11) => (鞋，click, 11, order,0, pay, 0)
    // (鞋，order, 12) => (鞋，click, 0, order,12, pay, 0)
    // (鞋，pay, 17)   => (鞋，click, 0, order,0, pay, 17)
    // => reduce
    // (鞋， click, 11, order,12, pay, 17)
    // reduce (A1, A1) => A1

    // TODO 将数据进行格式的转换
    // (category, count) => (category, clickcount, ordercount, paycount)
    val clickRDD =
    categoryClickReduceRDD.map {
      case (category, clickcount) =>
        (category, (clickcount, 0, 0))
    }
    val orderRDD =
      categoryOrderReduceRDD.map {
        case (category, ordercount) =>
          (category, (0, ordercount, 0))
      }
    val payRDD =
      categoryPayReduceRDD.map {
        case (category, paycount) =>
          (category, (0, 0, paycount))
      }

    val categoryRDD: RDD[(String, (Int, Int, Int))] =
      clickRDD.union(orderRDD).union(payRDD)

    // TODO 将合并的数据进行分组聚合
    // (鞋， (1, 12, 17))
    val categorySumRDD: RDD[(String, (Int, Int, Int))] = categoryRDD.reduceByKey(
      (c1, c2) => {
        (c1._1 + c2._1, c1._2 + c2._2, c1._3 + c2._3)
      }
    )

    // TODO 对分组聚合后的数据进行排序(降序)，并取前10个
    val tuples: Array[(String, (Int, Int, Int))] = categorySumRDD.sortBy(_._2, ascending = false).take(10)

    tuples
  }
}
