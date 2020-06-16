package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzBusiness(
                          @BeanProperty var businessid: Integer,
                          @BeanProperty var businessname: String,
                          @BeanProperty var sequence: String,
                          @BeanProperty var status: String,
                          @BeanProperty var creator: String,
                          @BeanProperty var createtime: String,
                          @BeanProperty var siteid: Integer,
                          @BeanProperty var dt: String,
                          @BeanProperty var dn: String
                        )
