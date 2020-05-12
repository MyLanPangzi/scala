#!/usr/bin/env bash
user=`whoami`

for i in master node1 node2 node3 ; do
  echo "----------$i----------------"
  ssh -t "$user@$i" "$@"
done
