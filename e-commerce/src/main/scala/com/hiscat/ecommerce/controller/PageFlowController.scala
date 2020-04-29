package com.hiscat.ecommerce.controller

import com.hiscat.ecommerce.common.TController
import com.hiscat.ecommerce.service.{HotCategoryAnalysisTop10Service, HotCategorySessionAnalysisTop10Service, PageFlowAnalysisService}


/**
 * 热门品类Top10控制器对象
 */
class PageFlowController extends TController {
  private val pageFlowAnalysisService = new PageFlowAnalysisService

  override def execute(): Unit = {
    this.pageFlowAnalysisService.analysis()
  }
}
