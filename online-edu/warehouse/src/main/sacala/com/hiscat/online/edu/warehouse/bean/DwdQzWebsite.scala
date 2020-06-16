package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzWebsite(
                         @BeanProperty var siteid: Integer,
                         @BeanProperty var sitename: String,
                         @BeanProperty var domain: String,
                         @BeanProperty var sequence: String,
                         @BeanProperty var multicastserver: String,
                         @BeanProperty var templateserver: String,
                         @BeanProperty var status: String,
                         @BeanProperty var creator: String,
                         @BeanProperty var createtime: String,
                         @BeanProperty var multicastgateway: String,
                         @BeanProperty var multicastport: String,
                         @BeanProperty var dt: String,
                         @BeanProperty var dn: String
                       )
