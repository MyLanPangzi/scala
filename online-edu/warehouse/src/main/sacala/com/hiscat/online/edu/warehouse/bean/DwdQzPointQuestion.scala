package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzPointQuestion(
                               @BeanProperty var pointid: Integer,
                               @BeanProperty var questionid: Integer,
                               @BeanProperty var questype: Integer,
                               @BeanProperty var creator: String,
                               @BeanProperty var createtime: String,
                               @BeanProperty var dt: String,
                               @BeanProperty var dn: String
                             )
