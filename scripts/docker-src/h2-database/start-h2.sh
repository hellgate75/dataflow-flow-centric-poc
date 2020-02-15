#!/bin/sh
mkdir -p /var/h2-database/logs
/bin/sh -c /var/h2-database/bin/h2.sh >> /var/h2-database/logs/h2-database.log *> /var/h2-database/logs/h2-database.log &
COUNTER=0
while [ ! -e /var/h2-database/logs/h2-database.log ] && [ $COUNTER -le 10  ] ; do
   sleep 1
   COUNTER=$((COUNTER + 1))
done
tail -f /var/h2-database/logs/h2-database.log