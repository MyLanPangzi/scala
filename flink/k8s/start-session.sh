#!/usr/bin/env bash

./bin/kubernetes-session.sh \
  -Dkubernetes.cluster-id=flink \
  -Dkubernetes.jobmanager.service-account=flink \
  -Dtaskmanager.memory.process.size=4096m \
  -Dkubernetes.taskmanager.cpu=2 \
  -Dtaskmanager.numberOfTaskSlots=4 \
  -Dresourcemanager.taskmanager-timeout=3600000

