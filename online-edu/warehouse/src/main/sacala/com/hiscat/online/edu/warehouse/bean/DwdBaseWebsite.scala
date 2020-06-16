package com.hiscat.online.edu.warehouse.bean

import java.sql.Timestamp

import scala.beans.BeanProperty

case class DwdBaseWebsite(
                        @BeanProperty var siteid: Int,
                        @BeanProperty var sitename: String,
                        @BeanProperty var siteurl: String,
                        @BeanProperty var delete: Int,
                        @BeanProperty var createtime: String,
                        @BeanProperty var creator: String,
                        @BeanProperty var dn: String
                      )
