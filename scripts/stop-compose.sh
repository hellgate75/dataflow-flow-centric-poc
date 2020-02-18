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

docker-compose stop
