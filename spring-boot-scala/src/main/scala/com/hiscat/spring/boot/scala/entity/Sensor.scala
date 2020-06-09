package com.hiscat.spring.boot.scala.entity

import com.baomidou.mybatisplus.annotation.{TableField, TableName}

@TableName("sensor")
case class Sensor(
                   @TableField("id")
                   val id: String,
                   @TableField("timestamp")
                   val timestamp: Long,
                   @TableField("temperature")
                   val temperature: Double
                 )
