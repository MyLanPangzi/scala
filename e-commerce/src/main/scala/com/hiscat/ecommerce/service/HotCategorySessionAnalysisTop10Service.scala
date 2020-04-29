package com.hiscat.ecommerce.service

import com.hiscat.ecommerce.bean
import com.hiscat.ecommerce.bean.UserVisitAction
import com.hiscat.ecommerce.common.TService
import com.hiscat.ecommerce.dao.HotCategorySessionAnalysisTop10Dao
import com.hiscat.ecommerce.util.ProjectUtil

/**
 * 热门品类Top10业务对象
 */
class HotCategorySessionAnalysisTop10Service extends TService {
  private val hotCategorySessionAnalysisTop10Dao = new HotCategorySessionAnalysisTop10Dao

  def analysis(top10Categories: Iterable[bean.HotCategory]): Array[(Long, List[(String, Int)])] = {
    val top10Ids = ProjectUtil.sparkContext().broadcast(top10Categories.map(_.id).toList)
    hotCategorySessionAnalysisTop10Dao.readFile("input/user_visit_action.txt")
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
      )
      .filter(e => top10Ids.value.contains(e.click_category_id.toString))
      //      .map(e => ((e.click_category_id, e.session_id), 1))
      //      .reduceByKey(_ + _)
      //      .map(e => (e._1._1, (e._1._2, e._2)))
      //      .groupByKey()
      //      .mapValues(_.toList.sortBy(_._2).reverse.take(10))
      .groupBy(_.click_category_id)
      .mapValues(v => {
        v.groupBy(_.session_id)
          .map {
            case (str, actions) => (str, actions.size)
          }
          .toList.sortBy(_._2).reverse.take(10)
      })
      .collect()

  }


}
