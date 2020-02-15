#!/bin/bash
PIPE=0
CAPPING=5
FLAG="0"
echo "Started ..."
while [ "0" = "$FLAG" ] && [ $PIPE -le 5  ] ; do 
   sleep 2
   PIPE=$((PIPE + 1))
   echo "Tick..."
done
echo "Finished"
