package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdMemberRegtype(
                          @BeanProperty var uid: Int,
                          @BeanProperty var appkey: String,
                          @BeanProperty var appregurl: String,
                          @BeanProperty var bdp_uuid: String,
                          @BeanProperty var createtime: String,
                          @BeanProperty var domain: String,
                          @BeanProperty var isranreg: String,
                          @BeanProperty var regsource: String,
                          @BeanProperty var regsourcename: String = "",
                          @BeanProperty var websiteid: Int,
                          @BeanProperty var dn: String,
                          @BeanProperty var dt: String
                        )
