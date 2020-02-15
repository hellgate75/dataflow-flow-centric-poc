#!/bin/sh
if [[ "" == "$RABBITMQ_RELEASE" ]]; then
	RABBITMQ_RELEASE=3.8-rc-management
fi
if [[ "" == "$RABBITMQ_RELEASE" ]]; then
	DOCKERHUB_USER=hellgate75
fi
docker --debug image build --rm . -t $DOCKERHUB_USER/rabbitmq:$RABBITMQ_RELEASE-flow-centric