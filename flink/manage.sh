#!/usr/bin/env bash
./bin/flink info ./examples/batch/WordCount.jar \
--input file:///home/user/hamlet.txt --output file:///home/user/wordcount_out

./bin/flink list
./bin/flink list -s
./bin/flink list -r
./bin/flink list -a
./bin/flink list -m yarn-cluster -yid <yarnApplicationID> -r
./bin/flink cancel <jobID>
./bin/flink cancel -s [targetDirectory] <jobID>
./bin/flink stop [-p targetDirectory] [-d] <jobID>

./bin/flink savepoint <jobId> [savepointDirectory]
./bin/flink savepoint <jobId> [savepointDirectory] -yid <yarnAppId>
./bin/flink stop [-p targetDirectory] [-d] <jobID>
./bin/flink cancel -s [savepointDirectory] <jobID>
./bin/flink run -s <savepointPath> ...
./bin/flink run -s <savepointPath> -n ...
./bin/flink savepoint -d <savepointPath>
./bin/flink savepoint -d <savepointPath> -j <jarFile>
