package com.hiscat.ecommerce.controller

import com.hiscat.ecommerce.common.TController
import com.hiscat.ecommerce.service.{HotCategoryAnalysisTop10Service, HotCategorySessionAnalysisTop10Service}


/**
 * 热门品类Top10控制器对象
 */
class HotCategorySessionAnalysisTop10Controller extends TController {
  private val hotCategorySessionAnalysisTop10Service = new HotCategorySessionAnalysisTop10Service
  private val hotCategoryAnalysisTop10Service = new HotCategoryAnalysisTop10Service

  override def execute(): Unit = {
    val top10Categories = hotCategoryAnalysisTop10Service.analysis5()
    this.hotCategorySessionAnalysisTop10Service.analysis(top10Categories).foreach(println)
  }
}
