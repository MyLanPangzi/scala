package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzMajor(
                       @BeanProperty var majorid: Integer,
                       @BeanProperty var businessid: Integer,
                       @BeanProperty var siteid: Integer,
                       @BeanProperty var majorname: String,
                       @BeanProperty var shortname: String,
                       @BeanProperty var status: String,
                       @BeanProperty var sequence: String,
                       @BeanProperty var creator: String,
                       @BeanProperty var createtime: String,
                       @BeanProperty var column_sitetype: String,
                       @BeanProperty var dt: String,
                       @BeanProperty var dn: String
                     )
