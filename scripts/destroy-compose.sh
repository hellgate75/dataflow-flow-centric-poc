#!/bin/sh

PWD="$(pwd)"
FOLDER="$(dirname "$(realpath "$0")")"

DOCKER="yes"
DOCKER_COMPOSE="yes"
if [[ "" = "$(which docker)" ]]; then
	DOCKER="no"
	echo ""
	echo "No docker binaries found ..."
	echo ""
fi

if [[ "" = "$(which docker-compose)" ]]; then
	echo ""
	DOCKER_COMPOSE="no"
	echo "No docker compose binaries found ..."
	echo ""
fi

if [[ "no" == "$DOCKER" ]]; then
	echo "Please install docker or run the PoC , that will help you with this task!!"
	exit 1
fi

if [[ "no" == "$DOCKER_COMPOSE" ]]; then
	echo "Please install odocker compose!!"
	exit 2
fi

if [[ "" == "$(docker network ls|grep flowcentric)" ]]; then
	echo "Detected missing of basic components that means the docker compose is not created yet!!"
	exit 1
fi

if [[ "" == "$(docker ps|grep rabbitmq|grep 3.8-rc-management-flow-centric)" ]]; then
	sh $FOLDER/stop-compose.sh
fi
docker-compose down -v --remove-orphans

