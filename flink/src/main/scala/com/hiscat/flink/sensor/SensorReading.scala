package com.hiscat.flink.sensor

import scala.beans.BeanProperty

case class SensorReading(
                          @BeanProperty id: String,
                          @BeanProperty timestamp: Long,
                          @BeanProperty temperature: Double)