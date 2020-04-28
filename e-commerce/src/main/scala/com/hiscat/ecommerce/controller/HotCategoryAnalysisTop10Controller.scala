package com.hiscat.ecommerce.controller

import com.hiscat.ecommerce.common.TController
import com.hiscat.ecommerce.service.HotCategoryAnalysisTop10Service


/**
 * 热门品类Top10控制器对象
 */
class HotCategoryAnalysisTop10Controller extends TController {
  private val hotCategoryAnalysisTop10Service = new HotCategoryAnalysisTop10Service

  override def execute(): Unit = {
    //        val result = hotCategoryAnalysisTop10Service.analysis2()
    //        val result = hotCategoryAnalysisTop10Service.analysis3()
    //        val result = hotCategoryAnalysisTop10Service.analysis4()
    hotCategoryAnalysisTop10Service.analysis5().foreach(println)
  }
}
