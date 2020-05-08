package com.hiscat.spark.sql

import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, MapType, StringType, StructField, StructType}
import org.apache.spark.sql.{Encoder, Encoders, Row, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Hive1 {

  case class Record(key: Int, value: String)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Hive1")
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql

    spark.udf.register("cityMark", AreaPercentUDAF)

    sql(
      """
        | select area, product_name, count(*) clicks, cityMark(city_name) cityMark
        |         from user_visit_action u
        |                  inner join city_info ci on u.city_id = ci.city_id
        |                  inner join product_info p on u.click_product_id = p.product_id
        |         where click_product_id != -1
        |         group by area, product_name, city_name
        |              """.stripMargin)
      .createOrReplaceTempView("t")

    spark.sql(
      """
        | select  area, product_name, clicks, cityMark
        |    from (
        |             select area, product_name, clicks, cityMark,  rank() over (PARTITION BY area order by clicks desc ) rank
        |             from t
        |         ) t
        | where t.rank <= 3
        |         """.stripMargin)
      .show(1000, truncate = false)
    spark.stop()
  }

  object AreaPercentUDAF extends UserDefinedAggregateFunction {
    override def inputSchema: StructType = StructType(StructField("city", StringType) :: Nil)

    override def bufferSchema: StructType =
      StructType(StructField("total", LongType) :: StructField("cityCount", MapType(StringType, LongType)) :: Nil)

    override def dataType: DataType = StringType

    override def deterministic: Boolean = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0L
      buffer(1) = Map[String, Long]()
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer(0) = buffer.getLong(0) + 1
      val map = buffer.getAs[Map[String, Long]](1)
      buffer(1) = map.updated(input.getString(0), map.getOrElse(input.getString(0), 0L) + 1)
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
      buffer2.getAs[Map[String, Long]](1).foreach {
        case (city, clicks) =>
          val map = buffer1.getAs[Map[String, Long]](1)
          buffer1(1) = map.updated(city, map.getOrElse(city, 0L) + clicks)
      }
    }

    override def evaluate(buffer: Row): String = {
      val sorted = buffer
        .getAs[mutable.Map[String, Long]](1)
        .toList
        .sortBy(_._2)
        .reverse
      val total = buffer.getLong(0).toDouble
      val tuples = sorted.take(2).map(e => (e._1, (e._2 / total * 100) + "%")) :+ ("其他", (sorted.drop(2).map(_._2).sum / total * 100) + "%")
      sorted.take(2).map(e => (e._1, (e._2 / total * 100) + "%")).mkString(",")
//      tuples.mkString(",")
    }
  }

  object AreaPercent extends Aggregator[CityClickEvent, mutable.Map[String, Long], String] {

    override def zero: mutable.Map[String, Long] = mutable.Map[String, Long]()

    override def reduce(b: mutable.Map[String, Long], a: CityClickEvent): mutable.Map[String, Long] = {
      b(a.city) = b.getOrElseUpdate(a.city, 0L) + a.clicks
      b
    }

    override def merge(b1: mutable.Map[String, Long], b2: mutable.Map[String, Long]): mutable.Map[String, Long] = {
      b2.foreach {
        case (city, clicks) => b1(city) = b1.getOrElseUpdate(city, 0L) + clicks
      }
      b1
    }

    override def finish(result: mutable.Map[String, Long]): String = {
      val sorted = result.toList.sortBy(_._2).reverse
      val sum = sorted.map(_._2).sum
      val list = sorted.take(2).map(e => (e._1, (e._2 / sum.toDouble * 100) + "%"))

      (list :+ ("其他", (sorted.drop(2).map(_._2).sum / sum.toDouble) + "%")).mkString(",")
    }

    override def bufferEncoder: Encoder[mutable.Map[String, Long]] = Encoders.kryo[mutable.Map[String, Long]]

    override def outputEncoder: Encoder[String] = Encoders.STRING
  }

  case class CityClickEvent(area: String, city: String, productName: String, clicks: Long)


}
