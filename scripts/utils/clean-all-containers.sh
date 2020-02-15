#!/bin/sh
LIST="$(docker ps -a|grep -v IMAGE | awk 'BEGIN {FS=OFS=" "}{print $1}'|xargs echo)"
IFS=" ";for id in $LIST; do
      docker stop $id
      if [[ "[]" != "$(docker inspect $id 2>/dev/null)" ]]; then
      	docker rm $id
      fi
done
