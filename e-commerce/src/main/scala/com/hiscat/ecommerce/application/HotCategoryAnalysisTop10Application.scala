package com.hiscat.ecommerce.application

import com.hiscat.ecommerce.common.TApplication
import com.hiscat.ecommerce.controller.HotCategoryAnalysisTop10Controller


/**
  * 热门品类Top10应用
  */
object HotCategoryAnalysisTop10Application extends App with TApplication{

    start( appName = "HotCategoryAnalysisTop10" ) {
        val controller = new HotCategoryAnalysisTop10Controller
        controller.execute()
    }

}
