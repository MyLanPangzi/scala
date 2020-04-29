package com.hiscat.ecommerce.service

import com.hiscat.ecommerce.bean
import com.hiscat.ecommerce.bean.UserVisitAction
import com.hiscat.ecommerce.common.TService
import com.hiscat.ecommerce.dao.{HotCategorySessionAnalysisTop10Dao, PageFlowAnalysisDao}
import com.hiscat.ecommerce.util.ProjectUtil
import org.apache.spark.rdd.RDD

/**
 * 热门品类Top10业务对象
 */
class PageFlowAnalysisService extends TService {
  private val pageFlowAnalysisDao = new PageFlowAnalysisDao

  override def analysis(): Any = {
    val ids = ProjectUtil.sparkContext().broadcast(List(1, 2, 3, 4, 5, 6, 7))
    val events = pageFlowAnalysisDao
      .readFile("input/user_visit_action.txt")
      .map(_.split("_"))
      .map(e =>
        UserVisitAction(
          date = e(0),
          user_id = e(1).toLong,
          session_id = e(2),
          page_id = e(3).toLong,
          action_time = e(4),
          search_keyword = e(5),
          click_category_id = e(6).toLong,
          click_product_id = e(7).toLong,
          order_category_ids = e(8),
          order_product_ids = e(9),
          pay_category_ids = e(10),
          pay_product_ids = e(11),
          city_id = e(12).toLong
        )
      ).cache()

    events
      .groupBy(_.session_id)
      .mapValues(v => {
        val actions = v.toList.sortBy(_.action_time)
        val condition = ids.value.zip(ids.value.tail)
        actions
          .zip(actions.tail)
          .map(e => (e._1.page_id, e._2.page_id))
          .filter(condition.contains)
          .map((_, 1))
      })
      .map(_._2)
      .flatMap(e => e)
      .reduceByKey(_ + _)
      .map(e => (e._1._1, e))
      .join(
        events
          .map(_.click_category_id)
          .filter(ids.value.contains)
          .map((_, 1))
          .reduceByKey(_ + _)
      )
      .map(e => (e._2._1._1, e._2._1._2 / e._2._2.toDouble))
      .collect()
      .foreach(println)

  }


}
