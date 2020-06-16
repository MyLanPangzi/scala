package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzChapter(
                         @BeanProperty var chapterid: Integer,
                         @BeanProperty var chapterlistid: Integer,
                         @BeanProperty var chaptername: String,
                         @BeanProperty var sequence: String,
                         @BeanProperty var showstatus: String,
                         @BeanProperty var creator: String,
                         @BeanProperty var createtime: String,
                         @BeanProperty var courseid: Integer,
                         @BeanProperty var chapternum: Integer,
                         @BeanProperty var outchapterid: Integer,
                         @BeanProperty var dt: String,
                         @BeanProperty var dn: String
                       )
