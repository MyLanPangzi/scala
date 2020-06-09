package com.hiscat.spring.boot.scala.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.hiscat.spring.boot.scala.entity.Sensor
import com.hiscat.spring.boot.scala.mapper.SensorMapper
import com.hiscat.spring.boot.scala.service.SensorService
import org.springframework.stereotype.Service

@Service
class SensorServiceImpl extends ServiceImpl[SensorMapper,Sensor] with SensorService{

}
