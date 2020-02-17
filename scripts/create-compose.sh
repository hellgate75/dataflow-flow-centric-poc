#!/bin/sh
#docker-compose up -d --remove-orphans --force-recreate --always-recreate-deps
if [[ "" != "$(docker network ls|grep flowcentric)" ]]; then
	echo "Detected components that means the docker compose is already created!!"
	exit 1
fi
docker-compose up -d --remove-orphans 
exit 0

