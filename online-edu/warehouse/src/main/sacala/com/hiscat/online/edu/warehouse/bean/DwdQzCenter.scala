package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzCenter(
                        @BeanProperty var centerid: Integer,
                        @BeanProperty var centername: String,
                        @BeanProperty var centeryear: String,
                        @BeanProperty var centertype: String,
                        @BeanProperty var openstatus: String,
                        @BeanProperty var centerparam: String,
                        @BeanProperty var description: String,
                        @BeanProperty var creator: String,
                        @BeanProperty var createtime: String,
                        @BeanProperty var sequence: String,
                        @BeanProperty var provideuser: String,
                        @BeanProperty var centerviewtype: String,
                        @BeanProperty var stage: String,
                        @BeanProperty var dt: String,
                        @BeanProperty var dn: String
                      )
