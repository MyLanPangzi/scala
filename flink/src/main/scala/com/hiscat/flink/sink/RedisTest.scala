package com.hiscat.flink.sink

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

object RedisTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    class RedisExampleMapper extends RedisMapper[(String, String)] {
      override def getCommandDescription: RedisCommandDescription = {
        new RedisCommandDescription(RedisCommand.HSET, "person")
      }

      override def getKeyFromData(data: (String, String)): String = data._1

      override def getValueFromData(data: (String, String)): String = data._2
    }
    val conf = new FlinkJedisPoolConfig.Builder().setHost("hadoop102").build()
    env.socketTextStream("hadoop102", 6000)
      .map(_.split(" "))
      .map(e => (e(0), e(1)))
      .addSink(new RedisSink[(String, String)](conf, new RedisExampleMapper))

    env.execute()
  }
}
