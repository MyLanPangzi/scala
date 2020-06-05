package com.hiscat.flink.ecommerce

case class ItemViewCount(
                       var   itemId: Long,
                       var  windowEnd: Long,
                       var  count: Long)
 