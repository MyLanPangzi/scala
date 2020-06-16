package com.hiscat.online.edu.warehouse.bean

import scala.beans.BeanProperty

case class DwdQzMemberPaperQuestion(
                                     @BeanProperty var userid: Integer,
                                     @BeanProperty var paperviewid: Integer,
                                     @BeanProperty var chapterid: Integer,
                                     @BeanProperty var sitecourseid: Integer,
                                     @BeanProperty var questionid: Integer,
                                     @BeanProperty var majorid: Integer,
                                     @BeanProperty var useranswer: String,
                                     @BeanProperty var istrue: String,
                                     @BeanProperty var lasttime: String,
                                     @BeanProperty var opertype: String,
                                     @BeanProperty var paperid: Integer,
                                     @BeanProperty var spendtime: Integer,
                                     @BeanProperty var score: Double,
                                     @BeanProperty var question_answer: Integer,
                                     @BeanProperty var dt: String,
                                     @BeanProperty var dn: String
                                   )
