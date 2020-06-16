package com.hiscat.online.edu.warehouse.service

import com.hiscat.online.edu.warehouse.bean.DwsMember
import org.apache.spark.sql.{SaveMode, SparkSession}

object DwsMemberService {

  def importMember(spark: SparkSession, date: String): Unit = {
    import spark.implicits._
    val member = DwdMemberDao.getDwdMember(spark).where(s"dt = $date")
    val reg = DwdMemberDao.getDwdMemberRegtype(spark)
    val ad = DwdMemberDao.getDwdBaseAd(spark)
    val site = DwdMemberDao.getDwdBaseWebsite(spark)
    val vip = DwdMemberDao.getDwdVipLevel(spark)
    val pay = DwdMemberDao.getDwdPcentermemePayMoney(spark)

    val uidAndDn = Seq("uid", "dn")
    val left = "left"
    member.join(reg, uidAndDn, left)
      .join(pay, uidAndDn, left)
      .join(site, Seq("siteid", "dn"), left)
      .join(vip, Seq("vip_id", "dn"), left)
      .join(ad, Seq("ad_id", "dn"), left)
      .select(
        "uid",
        "ad_id",
        "fullname",
        "iconurl",
        "lastlogin",
        "mailaddr",
        "memberlevel",
        "password",
        "paymoney",
        "phone",
        "qq",
        "register",
        "regupdatetime",
        "unitname",
        "userip",
        "zipcode",
        "appkey",
        "appregurl",
        "bdp_uuid",
        "reg_createtime",
        "domain",
        "isranreg",
        "regsource",
        "regsourcename",
        "adname",
        "siteid",
        "sitename",
        "siteurl",
        "site_delete",
        "site_createtime",
        "site_creator",
        "vip_id",
        "vip_level",
        "vip_start_time",
        "vip_end_time",
        "vip_last_modify_time",
        "vip_max_free",
        "vip_min_free",
        "vip_next_level",
        "vip_operator",
        "dt",
        "dn"
      )
      .as[DwsMember]
      .groupByKey(m => (m.uid, m.dn))
      .mapGroups((uidAndDn, it) => {
        val list = it.toList
        val head = list.head
        DwsMember(
          uid = uidAndDn._1,
          ad_id = head.ad_id,
          fullname = head.fullname,
          iconurl = head.iconurl,
          lastlogin = head.lastlogin,
          mailaddr = head.mailaddr,
          memberlevel = head.memberlevel,
          password = head.password,
          paymoney = list.map(_.paymoney).filter(_ != null).map(BigDecimal(_)).sum.toString(),
          phone = head.phone,
          qq = head.qq,
          register = head.register,
          regupdatetime = head.regupdatetime,
          unitname = head.unitname,
          userip = head.userip,
          zipcode = head.zipcode,
          appkey = head.appkey,
          appregurl = head.appregurl,
          bdp_uuid = head.bdp_uuid,
          reg_createtime = head.reg_createtime,
          domain = head.domain,
          isranreg = head.isranreg,
          regsource = head.regsource,
          regsourcename = head.regsourcename,
          adname = head.adname,
          siteid = head.siteid,
          sitename = head.sitename,
          siteurl = head.siteurl,
          site_delete = head.site_delete,
          site_createtime = head.site_createtime,
          site_creator = head.site_creator,
          vip_id = list.map(_.vip_id).max,
          vip_level = list.maxBy(_.vip_id).vip_level,
          vip_start_time = list.map(_.vip_start_time).min,
          vip_end_time = list.map(_.vip_end_time).max,
          vip_last_modify_time = list.map(_.vip_last_modify_time).max,
          vip_max_free = head.vip_max_free,
          vip_min_free = head.vip_min_free,
          vip_next_level = head.vip_next_level,
          vip_operator = head.vip_operator,
          dt = head.dt,
          dn = head.dn
        )
      })
      .coalesce(2)
      .write
      .mode(SaveMode.Overwrite)
      .insertInto("dws.dws_member")
  }

}
