package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzQuestionType(
                              @BeanProperty var quesviewtype: Integer,
                              @BeanProperty var viewtypename: String,
                              @BeanProperty var questypeid: Integer,
                              @BeanProperty var description: String,
                              @BeanProperty var status: String,
                              @BeanProperty var creator: String,
                              @BeanProperty var createtime: String,
                              @BeanProperty var papertypename: String,
                              @BeanProperty var sequence: String,
                              @BeanProperty var remark: String,
                              @BeanProperty var splitscoretype: String,
                              @BeanProperty var dt: String,
                              @BeanProperty var dn: String
                            )
