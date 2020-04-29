package com.hiscat.ecommerce.application

import com.hiscat.ecommerce.common.TApplication
import com.hiscat.ecommerce.controller.{HotCategoryAnalysisTop10Controller, HotCategorySessionAnalysisTop10Controller}


/**
  * 热门品类Top10应用
  */
object HotCategorySessionAnalysisTop10Application extends App with TApplication{

    start( appName = "HotCategorySessionAnalysisTop10Application" ) {
        val controller = new HotCategorySessionAnalysisTop10Controller
        controller.execute()
    }

}
