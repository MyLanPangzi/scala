package com.hiscat.online.edu.warehouse.service

import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.{JSON, JSONValidator}
import com.hiscat.online.edu.warehouse.bean.{DwdBaseAdLog, DwdMemberRegtype, DwdPcentermemPayMoney}
import org.apache.spark.sql.{SaveMode, SparkSession}

object EtlService {
  def etlPcenterMemPayMoney(spark: SparkSession): Unit = {
    import spark.implicits._
    spark.sparkContext
      .textFile("/log/user/pcentermempaymoney.log")
      .filter(JSONValidator.from(_).validate())
      //      .map(JSON.parseObject(_, classOf[PcentermemPayMoney]))
      .mapPartitions(_.map(s => {
        val json = JSON.parseObject(s, Feature.AllowArbitraryCommas)
        (
          json.getInteger("uid"),
          json.getString("paymoney"),
          json.getInteger("siteid"),
          json.getInteger("vip_id"),
          json.getString("dt"),
          json.getString("dn")
        )
      }))
      .toDF()
      .coalesce(1)
      .write
      .mode(SaveMode.Append)
//      .partitionBy("dt", "dn")
      .insertInto("dwd.dwd_pcentermempaymoney")

  }


  def etlMember(spark: SparkSession): Unit = {
    import spark.implicits._
    spark.sparkContext
      .textFile("/log/user/member.log")
      .filter(JSONValidator.from(_).validate())
      .mapPartitions(_.map(s => {
        val json = JSON.parseObject(s, Feature.AllowArbitraryCommas)
        var name = json.getString("fullname")
        name = name.head + "**"
        var phone = json.getString("phone")
        phone = phone.take(3) + "*" * 4 + phone.substring(8)
        val password = "********"
        (
          json.getInteger("uid"),
          json.getInteger("ad_id"),
          json.getString("birthday"),
          json.getString("email"),
          name,
          json.getString("iconurl"),
          json.getString("lastlogin"),
          json.getString("mailaddr"),
          json.getString("memberlevel"),
          password,
          json.getString("paymoney"),
          phone,
          json.getString("qq"),
          json.getString("register"),
          json.getString("regupdatetime"),
          json.getString("unitname"),
          json.getString("userip"),
          json.getString("zipcode"),
          json.getString("dt"),
          json.getString("dn")
        )
      }))
      .toDF()
      .coalesce(1)
      .write
      .mode(SaveMode.Append)
      .insertInto("dwd.dwd_member")
  }

  def etlMemberRegtype(spark: SparkSession): Unit = {
    import spark.implicits._
    spark.sparkContext
      .textFile("/log/user/memberRegtype.log")
      .filter(JSONValidator.from(_).validate())
      //      .map(JSON.parseObject(_, classOf[MemberRegtype]))
      .map(s => {
        val json = JSON.parseObject(s, Feature.AllowArbitraryCommas)

        val source = json.getString("regsource")
        val name = source match {
          case "1" => "PC"
          case "2" => "Mobile"
          case "3" => "App"
          case "4" => "WeChat"
          case _ => "other"
        }
        (
          json.getInteger("uid"),
          json.getString("appkey"),
          json.getString("appregurl"),
          json.getString("bdp_uuid"),
          json.getString("createtime"),
          json.getString("domain"),
          json.getString("isranreg"),
          source,
          name,
          json.getInteger("websiteid"),
          json.getString("dt"),
          json.getString("dn")
        )
      })
      .toDF()
      .coalesce(1)
      .write
      .mode(SaveMode.Append)
      .insertInto("dwd.dwd_member_regtype")
  }

  def etlVipLevel(spark: SparkSession): Unit = {
    import spark.implicits._
    spark.sparkContext
      .textFile("/log/user/pcenterMemViplevel.log")
      .filter(JSONValidator.from(_).validate())
      //      .map(JSON.parseObject(_, classOf[VipLevel]))
      .map(s => {
        val json = JSON.parseObject(s, Feature.AllowArbitraryCommas)
        (
          json.getInteger("vip_id"),
          json.getString("vip_level"),
          json.getString("start_time"),
          json.getString("end_time"),
          json.getString("last_modify_time"),
          json.getString("max_free"),
          json.getString("min_free"),
          json.getString("next_level"),
          json.getString("operator"),
          json.getString("dn")
        )
      })
      .toDF()
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .insertInto("dwd.dwd_vip_level")
  }

  def etlBaseWebsite(spark: SparkSession): Unit = {
    import spark.implicits._
    spark.sparkContext
      .textFile("/log/user/baswewebsite.log")
      .filter(JSONValidator.from(_).validate())
      .map(s => {
        val json = JSON.parseObject(s, Feature.AllowArbitraryCommas)
        (
          json.getInteger("siteid"),
          json.getString("sitename"),
          json.getString("siteurl"),
          json.getInteger("delete"),
          json.getString("createtime"),
          json.getString("creator"),
          json.getString("dn")
        )
      })
      .toDF()
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .insertInto("dwd.dwd_base_website")
  }


  def etlBaseAdLog(spark: SparkSession): Unit = {
    import spark.implicits._
    spark.sparkContext
      .textFile("/log/user/baseadlog.log")
      .filter(JSONValidator.from(_).validate())
      .map(JSON.parseObject(_, classOf[DwdBaseAdLog]))
      .toDS()
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .insertInto("dwd.dwd_base_ad")
  }

}
