package com.hiscat.spark.sql

import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator

object TypeSafeUDAF {

  case class Employee(var name: String, var salary: Long)

  case class Average(var sum: Long, var count: Long)

  object MyAvg extends Aggregator[Employee, Average, Double] {
    override def zero: Average = Average(0, 0)

    override def reduce(buffer: Average, employee: Employee): Average = {
      buffer.sum += employee.salary
      buffer.count += 1
      buffer
    }

    override def merge(b1: Average, b2: Average): Average = {
      b1.count += b2.count
      b1.sum += b2.sum
      b1
    }

    override def finish(reduction: Average): Double = reduction.sum.toDouble / reduction.count

    override def bufferEncoder: Encoder[Average] = Encoders.product

    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("TypeSafeUDAF")
      .getOrCreate()

    import spark.implicits._
    val ds = spark.read.json("input/employees.json").as[Employee]
    ds.show()
    val averageSalary = MyAvg.toColumn.name("average_salary")
    val result = ds.select(averageSalary)
    result.show()

    spark.stop()
  }
}
