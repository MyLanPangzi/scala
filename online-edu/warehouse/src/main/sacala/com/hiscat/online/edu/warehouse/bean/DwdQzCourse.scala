package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzCourse(
                        @BeanProperty var courseid:Integer,
                        @BeanProperty var majorid:Integer,
                        @BeanProperty var coursename:String,
                        @BeanProperty var coursechapter:String,
                        @BeanProperty var sequence:String,
                        @BeanProperty var isadvc:String,
                        @BeanProperty var creator:String,
                        @BeanProperty var createtime:String,
                        @BeanProperty var status:String,
                        @BeanProperty var chapterlistid:Integer,
                        @BeanProperty var pointlistid:Integer,
                        @BeanProperty var dt:String,
                        @BeanProperty var dn:String
                      )
