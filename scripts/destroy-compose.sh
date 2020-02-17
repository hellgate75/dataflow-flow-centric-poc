#!/bin/sh
if [[ "" == "$(docker network ls|grep flowcentric)" ]]; then
	echo "Detected missing of basic components that means the docker compose is not created yet!!"
	exit 1
fi
docker-compose down -v --remove-orphans

