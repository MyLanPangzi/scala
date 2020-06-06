package com.hiscat.flink.source

import java.util.UUID

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import scala.util.Random

class MarketingUserBehaviorSource extends RichParallelSourceFunction[MarketingUserBehavior] {

  var running = true
  val rand = new Random()
  val channels = Seq("xiaomi", "app", "huawei")
  val behaviors = Seq("install", "uninstall", "browse", "click")

  override def run(ctx: SourceFunction.SourceContext[MarketingUserBehavior]): Unit = {
    while (running) {
      ctx.collect(MarketingUserBehavior(
        userId = UUID.randomUUID().toString,
        behavior = behaviors(rand.nextInt(behaviors.length)),
        channel = channels(rand.nextInt(channels.length)),
        ts = System.currentTimeMillis()))
      Thread.sleep(10)
    }
  }

  override def cancel(): Unit = running = false
}
