package com.hiscat.spark.streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ConnectException, Socket}
import java.nio.charset.StandardCharsets
import java.time.Instant

import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

object CustomerReceiver {
  def main(args: Array[String]): Unit = {
    val ssc = new StreamingContext(
      new SparkConf().setAppName("CustomerReceiver").setMaster("local[*]"),
      Seconds(5)
    )

    ssc.receiverStream(new MyReceiver)
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .foreachRDD((rdd, t) => {
        println(Instant.ofEpochMilli(t.milliseconds))
        rdd.foreachPartition(it => {
          it.foreach(println)
        })
      })

    ssc.start()
    ssc.awaitTermination()
  }

  class MyReceiver(host: String = "localhost", port: Int = 9999)
    extends Receiver[String](StorageLevel.MEMORY_ONLY_SER) {
    override def onStart(): Unit = {
      new Thread("Socket Receiver") {
        override def run(): Unit = {
          receive()
        }
      }.start()
    }

    def receive(): Unit = {
      var socket: Socket = null
      var userInput: String = null
      try {
        println(s"Connecting to $host : $port")
        socket = new Socket(host, port)
        println(s"Connected to $host : $port")
        val reader = new BufferedReader(
          new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8))
        userInput = reader.readLine()
        while (!isStopped() && userInput != null) {
          store(userInput)
          userInput = reader.readLine()
        }
        reader.close()
        socket.close()
        println("Stopped receiving")
        restart("Trying to connect again")
      } catch {
        case e: ConnectException =>
          restart(s"Error connecting to $host,$port", e)
      }
    }

    override def onStop(): Unit = {

    }
  }

}
