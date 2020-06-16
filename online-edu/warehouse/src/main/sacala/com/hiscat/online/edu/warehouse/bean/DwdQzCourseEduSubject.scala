package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzCourseEduSubject(
                                  @BeanProperty var courseeduid: Integer,
                                  @BeanProperty var edusubjectid: Integer,
                                  @BeanProperty var courseid: Integer,
                                  @BeanProperty var creator: String,
                                  @BeanProperty var createtime: String,
                                  @BeanProperty var majorid: Integer,
                                  @BeanProperty var dt: String,
                                  @BeanProperty var dn: String
                                )
