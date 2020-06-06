package com.hiscat.flink.source

case class MarketingUserBehavior(userId: String,
                                 behavior: String,
                                 channel: String,
                                 ts: Long)
