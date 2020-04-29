package com.hiscat.ecommerce.application

import com.hiscat.ecommerce.common.TApplication
import com.hiscat.ecommerce.controller.{HotCategorySessionAnalysisTop10Controller, PageFlowController}


/**
  * 热门品类Top10应用
  */
object PageFlowApplication extends App with TApplication{

    start( appName = "PageFlowApplication" ) {
        val controller = new PageFlowController
        controller.execute()
    }

}
