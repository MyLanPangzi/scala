package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdPcentermemPayMoney(
                               @BeanProperty var uid: Int,
                               @BeanProperty var paymoney: String,
                               @BeanProperty var siteid: Int,
                               @BeanProperty var vip_id: Int,
                               @BeanProperty var dt: String,
                               @BeanProperty var dn: String
                             )
