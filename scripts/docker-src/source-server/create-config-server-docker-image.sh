#!/bin/sh
if [[ "" == "$CONFIG_SERVER_RELEASE" ]]; then
	CONFIG_SERVER_RELEASE="1.0.0-flow-centric"
fi
if [[ "" == "$DOCKERHUB_USER" ]]; then
	DOCKERHUB_USER=hellgate75
fi
ln -s ../../../dataflow-ms-flow-centric-config-server/target/dataflow-ms-flow-centric-config-server-*.jar dataflow-ms-flow-centric-config-server.jar 
docker --debug image build --rm . -t $DOCKERHUB_USER/spring-cloud-config-server:$CONFIG_SERVER_RELEASE
rm dataflow-ms-flow-centric-config-server.jar