#!/bin/sh

mkdir -p $(pwd)/var

if [[ ! -e $(pwd)/var/flow-centric-config-server.pid ]]; then
	echo "Process Id file for Merge Layer Spring Cloud Config Server dosn't exists!!"
else
	PID="$(cat $(pwd)/var/flow-centric-config-server.pid)"
	kill -9 $PID
	rm -f $(pwd)/var/flow-centric-config-server.pid
	echo "Process $PID for Merge Layer Spring Cloud Config Server killed!!"
fi
