package com.hiscat.online.edu.warehouse.app

import com.alibaba.fastjson.JSON

object Json {
  def main(args: Array[String]): Unit = {
    val s="{\"ad_id\":\"9\",\"birthday\":\"1997-11-16\",\"dn\":\"webA\",\"dt\":\"20190722\",\"email\":\"test@126.com\",\"fullname\":\"王0\",\"iconurl\":\"-\",\"lastlogin\":\"-\",\"mailaddr\":\"-\",\"memberlevel\":\"1\",\"password\":\"123456\",\"paymoney\":\"-\",\"phone\":\"13711235451\",\"qq\":\"10000\",\"register\":\"2015-04-05\",\"regupdatetime\":\"-\",\"uid\":\"0\",\"unitname\":\"-\",\"userip\":\"222.42.116.199\",\"zipcode\":\"-\"}"
    import scala.collection.JavaConversions._
    JSON.parseObject(s).keySet().foreach(println(_))
  }
}
