package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzPaperView(
                           @BeanProperty var paperviewid: Integer,
                           @BeanProperty var paperid: Integer,
                           @BeanProperty var paperviewname: String,
                           @BeanProperty var paperparam: String,
                           @BeanProperty var openstatus: String,
                           @BeanProperty var explainurl: String,
                           @BeanProperty var iscontest: String,
                           @BeanProperty var contesttime: String,
                           @BeanProperty var conteststarttime: String,
                           @BeanProperty var contestendtime: String,
                           @BeanProperty var contesttimelimit: String,
                           @BeanProperty var dayiid: Integer,
                           @BeanProperty var status: String,
                           @BeanProperty var creator: String,
                           @BeanProperty var createtime: String,
                           @BeanProperty var paperviewcatid: Integer,
                           @BeanProperty var modifystatus: String,
                           @BeanProperty var description: String,
                           @BeanProperty var papertype: String,
                           @BeanProperty var downurl: String,
                           @BeanProperty var paperuse: String,
                           @BeanProperty var paperdifficult: String,
                           @BeanProperty var testreport: String,
                           @BeanProperty var paperuseshow: String,
                           @BeanProperty var dt: String,
                           @BeanProperty var dn: String
                         )
