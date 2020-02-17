#!/bin/sh
if [[ "" != "$(docker network ls|grep flowcentric)" ]]; then
   docker network rm flowcentric
fi
#if [[ "" != "$(docker network ls|grep scripts_frontend)" ]]; then
#   docker network rm scripts_frontend
#fi
#if [[ "" != "$(docker volume ls|grep scripts_frontend)" ]]; then
#   docker network rm scripts_frontend
#fi
#Creating volume "scripts_volume_rabbitmq" with default driver
#Creating volume "scripts_volume_data_mongodb" with default driver
#Creating volume "scripts_volume_config_mongodb" with default driver
#Creating volume "scripts_volume_h2_data" with default driver
#Creating volume "scripts_volume_ms_logs" with default driver
docker-compose up -d --remove-orphans --force-recreate --always-recreate-deps

