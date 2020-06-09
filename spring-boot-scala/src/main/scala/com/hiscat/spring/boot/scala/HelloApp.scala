package com.hiscat.spring.boot.scala

import com.hiscat.spring.boot.scala.entity.Sensor
import com.hiscat.spring.boot.scala.service.SensorService
import org.apache.spark.{SparkConf, SparkContext}
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.kafka.core.KafkaTemplate

import scala.collection.JavaConverters._

@SpringBootApplication
@MapperScan(Array("com.hiscat.spring.boot.scala.mapper"))
class HelloApp {
  @Autowired
  var sensorService: SensorService = _

  @Autowired
  var stringRedisTemplate: StringRedisTemplate = _

  //noinspection SpringJavaInjectionPointsAutowiringInspection
  @Autowired
  var kafkaTemplate: KafkaTemplate[String, String] = _

  def run(): Unit = {
    val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("spring-boot-scala-spark"))

    val sensor = sc.makeRDD(sensorService.list().asScala)
      .max()(Ordering.by[Sensor, Double](_.temperature))

    stringRedisTemplate.opsForValue().set(sensor.id, sensor.temperature.toString)
    println(stringRedisTemplate.opsForValue().get(sensor.id))
    kafkaTemplate.send("sensor", sensor.toString)

    sc.stop()
  }
}

object HelloApp extends App {
  SpringApplication
    .run(classOf[HelloApp])
    .getBean(classOf[HelloApp])
    .run()
}
