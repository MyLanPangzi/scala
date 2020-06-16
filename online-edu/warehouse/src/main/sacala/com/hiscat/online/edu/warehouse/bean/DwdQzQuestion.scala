package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzQuestion(
                          @BeanProperty var questionid: Integer,
                          @BeanProperty var parentid: Integer,
                          @BeanProperty var questypeid: Integer,
                          @BeanProperty var quesviewtype: Integer,
                          @BeanProperty var content: String,
                          @BeanProperty var answer: String,
                          @BeanProperty var analysis: String,
                          @BeanProperty var limitminute: String,
                          @BeanProperty var score: Double,
                          @BeanProperty var splitscore: Double,
                          @BeanProperty var status: String,
                          @BeanProperty var optnum: Integer,
                          @BeanProperty var lecture: String,
                          @BeanProperty var creator: String,
                          @BeanProperty var createtime: String,
                          @BeanProperty var modifystatus: String,
                          @BeanProperty var attanswer: String,
                          @BeanProperty var questag: String,
                          @BeanProperty var vanalysisaddr: String,
                          @BeanProperty var difficulty: String,
                          @BeanProperty var quesskill: String,
                          @BeanProperty var vdeoaddr: String,
                          @BeanProperty var dt: String,
                          @BeanProperty var dn: String
                        )
