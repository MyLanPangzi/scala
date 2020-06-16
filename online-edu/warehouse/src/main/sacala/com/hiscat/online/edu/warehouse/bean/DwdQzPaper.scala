package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzPaper(
                       @BeanProperty var paperid: Integer,
                       @BeanProperty var papercatid: Integer,
                       @BeanProperty var courseid: Integer,
                       @BeanProperty var paperyear: String,
                       @BeanProperty var chapter: String,
                       @BeanProperty var suitnum: String,
                       @BeanProperty var papername: String,
                       @BeanProperty var status: String,
                       @BeanProperty var creator: String,
                       @BeanProperty var createtime: String,
                       @BeanProperty var totalscore: Double,
                       @BeanProperty var chapterid: Integer,
                       @BeanProperty var chapterlistid: Integer,
                       @BeanProperty var dt: String,
                       @BeanProperty var dn: String
                     )
