package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdBaseAdLog(
                      @BeanProperty var adid: Int,
                      @BeanProperty var adname: String,
                      @BeanProperty var dn: String
                    )
