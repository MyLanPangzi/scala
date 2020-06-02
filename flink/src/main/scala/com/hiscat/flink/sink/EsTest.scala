package com.hiscat.flink.sink

import java.util

import com.hiscat.flink.sensor.SensorReading
import com.hiscat.flink.sensor.SensorTest.SensorSource
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

object EsTest {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val esSinkBuilder = new ElasticsearchSink.Builder[SensorReading](
      util.Arrays.asList(new HttpHost("hadoop102", 9200, "http")),
      new ElasticsearchSinkFunction[SensorReading] {
        def process(element: SensorReading, ctx: RuntimeContext, indexer: RequestIndexer) {
          val json = new java.util.HashMap[String, String]
          json.put("data", element.toString)
          json.put("id",element.id)

          val rqst: IndexRequest = Requests.indexRequest
            .index("sensor33")
            .`type`("_doc")
              .source(json)
          //            .source(JSON.parseObject(JSON.toJSONString(element, SerializerFeature.IgnoreErrorGetter), classOf[util.Map[String, String]]))

          indexer.add(rqst)
        }
      }
    )
    esSinkBuilder.setBulkFlushMaxActions(1)

    env.addSource(new SensorSource)
      .addSink(esSinkBuilder.build())

    env.execute()
  }
}
