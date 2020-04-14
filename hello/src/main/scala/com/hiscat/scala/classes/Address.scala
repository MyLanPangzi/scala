package com.hiscat.scala.classes

import scala.beans.BeanProperty

class Address(
               @BeanProperty var street1: String,
               @BeanProperty var street2: String,
               var city: String,
               var state: String,
             )
