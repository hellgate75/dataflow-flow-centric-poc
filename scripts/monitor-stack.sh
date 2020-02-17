#!/bin/sh
LIST="$(docker-compose ps)"; while (true) do clear; echo "$LIST"; sleep 5; export LIST="$(docker-compose ps)"; done
