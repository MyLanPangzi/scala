#!/usr/bin/env bash
./bin/flink run ./examples/batch/WordCount.jar
./bin/flink run ./examples/batch/WordCount.jar \
--input file:///home/user/hamlet.txt \
--output file:///home/user/wordcount_out

./bin/flink run -p 16 ./examples/batch/WordCount.jar \
--input file:///home/user/hamlet.txt \
--output file:///home/user/wordcount_out

./bin/flink run -q ./examples/batch/WordCount.jar
./bin/flink run -d ./examples/batch/WordCount.jar

./bin/flink run -m myJMHost:8081 \
./examples/batch/WordCount.jar \
--input file:///home/user/hamlet.txt --output file:///home/user/wordcount_out

./bin/flink run -c org.apache.flink.examples.java.wordcount.WordCount \
./examples/batch/WordCount.jar \
--input file:///home/user/hamlet.txt --output file:///home/user/wordcount_out

./bin/flink run -m yarn-cluster \
./examples/batch/WordCount.jar \
--input hdfs:///user/hamlet.txt --output hdfs:///user/wordcount_out

JOBMANAGER_CONTAINER=$(docker ps --filter name=jobmanager --format={{.ID}})
docker cp path/to/jar "$JOBMANAGER_CONTAINER":/job.jar
docker exec -t -i "$JOBMANAGER_CONTAINER" flink run /job.jar