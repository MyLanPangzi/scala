package com.hiscat.spark.sql

import java.io.File

import org.apache.spark.sql.catalyst.expressions.GenericRow
import org.apache.spark.sql.expressions.MutableAggregationBuffer
import org.apache.spark.sql.{Row, SaveMode, SparkSession}

object Hive {

  case class Record(key: Int, value: String)

  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Hive")
//      .config("spark.sql.hive.metastore.jars", "D:\\soft\\apache-hive-2.3.6\\lib\\*")
//      .config("spark.sql.hive.metastore.version", "2.3.3")
      .enableHiveSupport()
      .getOrCreate()
    import spark.implicits._
    import spark.sql

    sql("CREATE TABLE IF NOT EXISTS src(key INT, value STRING) USING hive")
    sql("LOAD DATA LOCAL INPATH  'input/kv1.txt' OVERWRITE INTO TABLE src")
    sql("SELECT * FROM src").show(3)
    sql("SELECT COUNT(*) FROM src").show(3)
    sql("SELECT key,value FROM src WHERE key < 10 ORDER BY key")
      .map {
        case Row(key: Int, value: String) => s"Key: $key, value:$value"
      }
      .show(3)

    spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
      .createOrReplaceTempView("records")
    sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show(3)

    sql("CREATE TABLE IF NOT EXISTS hive_records(key int, value string) STORED AS PARQUET")
    val df = spark.table("src")
    df.write
      .mode(SaveMode.Overwrite)
      .saveAsTable("hive_records")
    sql("SELECT * FROM hive_records").show(3)

    val dataDir = "/tmp/parquet_data"
    spark.range(10).write.mode(SaveMode.Overwrite).parquet(dataDir)
    sql(s"create external table if not exists hive_bigint(id bigint) stored as parquet location '$dataDir'")
    sql("select * from hive_bigint").show()

    spark.sqlContext.setConf("hive.exec.dynamic.partition", "true")
    spark.sqlContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")
    df.write.mode(SaveMode.Overwrite).partitionBy("key").format("hive").saveAsTable("hive_part_tbl")
    spark.sql("select * from hive_part_tbl").show()

    spark.stop()
  }
}
