#!/bin/sh
SCRIPT_NAME="start-dataflow-server.sh"
source ./.scriptsh
JAR="$(ls spring-cloud-dataflow-server-*.jar)"
#if [[ "" -eq "$JAR" ]]; then
#	echo "Unable to locate jar file: try download!!"
#	("$(pwd)/download-jars.sh")
#	ECODE="$@"
#	if [[ "0" != "$ECODE" ]]; then
#		exit $ECODE
#	fi
#	JAR="$(ls spring-cloud-dataflow-shell-*.jar)"
#	if [[ "" -eq "$JAR" ]]; then
#		echo "Unable to locate jar file: exit!!"
#		exit 1
#	fi
#	
#fi
if [[ -e /c/cygwin64/bin ]]; then
	export PATH=$PATH:/c/cygwin64/bin
fi

mkdir -p $(pwd)/log
mkdir -p $(pwd)/var

JAVA_SPRING_DEBUG="-Ddebug=true"
if [[ "--debug" != "$1" ]]; then
  JAVA_SPRING_DEBUG=""
fi
JAVA_ARGS=" -Xms512m -Xmx1g -XX:+UseConcMarkSweepGC $JAVA_SPRING_DEBUG -Dlogging.level.org.springframework.cloud=DEBUG -Dspring.profiles.active=local -Dspring.cloud.config.uri=http://localhost:8899"

SKIPPER_HOST=localhost
SKIPPER_PORT=7577
SKIPPER_PROTOCOL=http
java $JAVA_ARGS -jar $JAR --spring.cloud.skipper.client.serverUri=$SKIPPER_PROTOCOL://$SKIPPER_HOST:$SKIPPER_PORT/api  > $(pwd)/log/spring-cloud-dataflow-server.log &
PID="$(echo $!)"
echo "Started Spring Cloud Data Flow Server"
echo "Process Id: $PID"
echo "$PID" > $(pwd)/var/spring-cloud-dataflow-server.pid
