package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzChapterList(
                             @BeanProperty var chapterlistid: Integer,
                             @BeanProperty var chapterlistname: String,
                             @BeanProperty var courseid: Integer,
                             @BeanProperty var chapterallnum: Integer,
                             @BeanProperty var sequence: String,
                             @BeanProperty var status: String,
                             @BeanProperty var creator: String,
                             @BeanProperty var createtime: String,
                             @BeanProperty var dt: String,
                             @BeanProperty var dn: String
                           )
