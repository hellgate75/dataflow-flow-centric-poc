#!/bin/sh
LIST="$(ps -eaf|grep java|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}'|xargs echo)"
LIST="$LIST $(ps -eaf|grep tail|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}'|xargs echo)"
sh -c "kill $LIST" 
