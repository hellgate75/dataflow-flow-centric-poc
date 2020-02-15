#!/bin/sh
JAR="$(ls spring-cloud-skipper-shell-*.jar)"
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

echo "Starting Spring Cloud Skipper Shell"
java -jar $(pwd)/$JAR