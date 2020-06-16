package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzCenterPaper(
                             @BeanProperty var paperviewid: Integer,
                             @BeanProperty var centerid: Integer,
                             @BeanProperty var openstatus: String,
                             @BeanProperty var sequence: String,
                             @BeanProperty var creator: String,
                             @BeanProperty var createtime: String,
                             @BeanProperty var dt: String,
                             @BeanProperty var dn: String
                           )
