package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzPoint(
                       @BeanProperty var pointid: Integer,
                       @BeanProperty var courseid: Integer,
                       @BeanProperty var pointname: String,
                       @BeanProperty var pointyear: String,
                       @BeanProperty var chapter: String,
                       @BeanProperty var creator: String,
                       @BeanProperty var createtme: String,
                       @BeanProperty var status: String,
                       @BeanProperty var modifystatus: String,
                       @BeanProperty var excisenum: Integer,
                       @BeanProperty var pointlistid: Integer,
                       @BeanProperty var chapterid: Integer,
                       @BeanProperty var sequece: String,
                       @BeanProperty var pointdescribe: String,
                       @BeanProperty var pointlevel: String,
                       @BeanProperty var typelist: String,
                       @BeanProperty var score: Double,
                       @BeanProperty var thought: String,
                       @BeanProperty var remid: String,
                       @BeanProperty var pointnamelist: String,
                       @BeanProperty var typelistids: String,
                       @BeanProperty var pointlist: String,
                       @BeanProperty var dt: String,
                       @BeanProperty var dn: String
                     )
