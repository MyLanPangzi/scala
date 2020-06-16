package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzSiteCourse(
                            @BeanProperty var sitecourseid: Integer,
                            @BeanProperty var siteid: Integer,
                            @BeanProperty var courseid: Integer,
                            @BeanProperty var sitecoursename: String,
                            @BeanProperty var coursechapter: String,
                            @BeanProperty var sequence: String,
                            @BeanProperty var status: String,
                            @BeanProperty var creator: String,
                            @BeanProperty var createtime: String,
                            @BeanProperty var helppaperstatus: String,
                            @BeanProperty var servertype: String,
                            @BeanProperty var boardid: Integer,
                            @BeanProperty var showstatus: String,
                            @BeanProperty var dt: String,
                            @BeanProperty var dn: String
                          )
