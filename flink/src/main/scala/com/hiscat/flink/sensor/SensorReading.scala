package com.hiscat.flink.sensor

case class SensorReading(id: String,
                         timestamp: Long,
                         temperature: Double)