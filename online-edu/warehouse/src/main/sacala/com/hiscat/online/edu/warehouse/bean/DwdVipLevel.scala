package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdVipLevel(
                     @BeanProperty var vip_id: Int,
                     @BeanProperty var vip_level: String,
                     @BeanProperty var start_time: String,
                     @BeanProperty var end_time: String,
                     @BeanProperty var last_modify_time: String,
                     @BeanProperty var max_free: String,
                     @BeanProperty var min_free: String,
                     @BeanProperty var next_level: String,
                     @BeanProperty var operator: String,
                     @BeanProperty var dn: String
                   )
