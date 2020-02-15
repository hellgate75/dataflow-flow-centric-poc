#!/bin/sh
LIST="$(docker image ls|grep -v IMAGE| grep -i "<none>"| awk 'BEGIN {FS=OFS=" "}{print $3}'|xargs echo)"
IFS=" ";for id in $LIST; do
     docker rmi -f $id
done
