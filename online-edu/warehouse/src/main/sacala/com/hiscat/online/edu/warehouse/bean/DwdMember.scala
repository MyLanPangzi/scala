package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdMember(
                   @BeanProperty var uid: Int,
                   @BeanProperty var ad_id: Int,
                   @BeanProperty var birthday: String,
                   @BeanProperty var email: String,
                   @BeanProperty var fullname: String,
                   @BeanProperty var iconurl: String,
                   @BeanProperty var lastlogin: String,
                   @BeanProperty var mailaddr: String,
                   @BeanProperty var memberlevel: String,
                   @BeanProperty var password: String,
                   @BeanProperty var paymoney: String,
                   @BeanProperty var phone: String,
                   @BeanProperty var qq: String,
                   @BeanProperty var register: String,
                   @BeanProperty var regupdatetime: String,
                   @BeanProperty var unitname: String,
                   @BeanProperty var userip: String,
                   @BeanProperty var zipcode: String,
                   @BeanProperty var dt: String,
                   @BeanProperty var dn: String
                 )
