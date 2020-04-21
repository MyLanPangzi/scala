#!/usr/bin/env bash
bin/spark-submit \
  --class "com.hiscat.spark.SimpleApp" \
  --master spark://hadoop102:7077 \
  /opt/module/spark-local/examples/jars/spark-1.0-SNAPSHOT.jar

#  --master local[4] \
bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master spark://hadoop102:7077 \
./examples/jars/spark-examples_2.11-2.4.5.jar \
10

bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
./examples/jars/spark-examples_2.11-2.4.5.jar \
10
