#!/bin/sh
mkdir -p $(pwd)/var

if [[ ! -e $(pwd)/var/spring-cloud-skipper-server.pid ]]; then
	echo "Process Id file for Spring Cloud Skipper Server dosn't exists!!"
else
	PID="$(cat $(pwd)/var/spring-cloud-skipper-server.pid)"
	kill -9 $PID
	rm -f $(pwd)/var/spring-cloud-skipper-server.pid
	echo "Process $PID for Spring Cloud Skipper Server killed!!"
fi
