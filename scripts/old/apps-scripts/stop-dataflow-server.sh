#!/bin/sh

mkdir -p $(pwd)/var

if [[ ! -e $(pwd)/var/spring-cloud-dataflow-server.pid ]]; then
	echo "Process Id file for Spring Cloud Dataflow Server dosn't exists!!"
else
	PID="$(cat $(pwd)/var/spring-cloud-dataflow-server.pid)"
	kill -9 $PID
	rm -f $(pwd)/var/spring-cloud-dataflow-server.pid
	echo "Process $PID for Spring Cloud Dataflow Server killed!!"
fi
