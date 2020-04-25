package com.hiscat.spark.sql

import java.nio.file.{Files, Paths}

import org.apache.spark.sql.SparkSession

object Avro {
  def main(args: Array[String]): Unit = {
    Runtime.getRuntime.exec("D:\\soft\\Git\\usr\\bin\\rm -rf E:\\github\\scala\\output")
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Avro")
      .getOrCreate()
    import spark.implicits._
    import org.apache.spark.sql.avro._

    val users = spark.read.format("avro")
      .load("input/users.avro")
    users.show()
    users.select("name", "favorite_color")
      .write.format("avro")
      .save("output/namesAndFavColors.avro")

    import org.apache.spark.sql.avro._

    spark.read.format("avro")
      .load("input/users.avro")
    // `from_avro` requires Avro schema in JSON string format.


    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
      .option("subscribe", "topic1")
      .load()

    //    // 1. Decode the Avro data into a struct;
    //    // 2. Filter by column `favorite_color`;
    //    // 3. Encode the column `name` in Avro format.
    val schema = new String(Files.readAllBytes(Paths.get("E:\\github\\scala\\input\\users.avro")))
    val output = df
      .select(from_avro('value, schema) as 'user)
      .where("user.favorite_color == \"red\"")
      .select(to_avro($"user.name") as 'value)


    //    val query = output
    //      .writeStream
    //      .format("kafka")
    //      .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
    //      .option("topic", "topic2")
    //      .start()

    spark.stop()
  }
}
