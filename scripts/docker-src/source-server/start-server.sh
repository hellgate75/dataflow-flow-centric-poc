#!/bin/bash
source "$(which ms-env.sh)"
ARGS="$APP_ARGS"
mkdir -p /opt/$APP_GROUP/log
mkdir -p /opt/$APP_GROUP/log/$APP_NAME/
echo "Starting $APP_DESCR ..."
echo "JVM ARGS: $JVM_ARGS"
echo "APP ARGS: $ARGS"
echo ""
CMD="java $JVM_ARGS -jar /opt/$APP_GROUP/$APP_NAME/jar/$APP_NAME.jar $MAIN_CLASS $ARGS"
BOOTSTRAP_LOG_FILE="/opt/$APP_GROUP/log/$APP_NAME.log"
if [ "yes" == "$REDIRECT_OUTPUT_TO_APP" ]; then
	BOOTSTRAP_LOG_FILE="/opt/$APP_GROUP/log/$APP_NAME/$APP_NAME-$LOG_LEVEL.log"
	nohup execute.sh "$CMD" > $BOOTSTRAP_LOG_FILE 2>&1 &
else
	nohup execute.sh "$CMD" > $BOOTSTRAP_LOG_FILE 2>&1 &
fi
PID1="$(ps -eaf| grep java|grep -v /bin/sh|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}')"
PID2="$(ps -eaf| grep java|grep /bin/sh|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}')"
PID3="$(ps -eaf| grep tail|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}')"
echo ""
if [ "" != "$PID" ]; then
	mkdir -p /var/run/$APP_GROUP
	echo "$PID1 $PID2 $PID3" > /var/run/$APP_GROUP/$APP_NAME.pid
	echo "JVM Process Id: $PID1"
else
	echo "No Process Id. Application failed to start..."
fi
echo ""
echo "$APP_DESCR $LOG_LEVEL logs"
echo "====================================================================="
echo "Log file: /opt/$APP_GROUP/log/$APP_NAME/$APP_NAME-$LOG_LEVEL.log"
echo ""
COUNTER=0
while [ ! -e /opt/$APP_GROUP/log/$APP_NAME/$APP_NAME-$LOG_LEVEL.log ] && [ $COUNTER -le 200  ] ; do 
   sleep 5
   COUNTER=$((COUNTER + 1))
   printf "."
done
tail -f /opt/$APP_GROUP/log/$APP_NAME/$APP_NAME-$LOG_LEVEL.log
if [ ! -e /opt/$APP_GROUP/log/$APP_NAME/$APP_NAME-$LOG_LEVEL.log ]; then
	echo "Log file doesn;t exists. Taking up the container to check the problem"
	echo "type :"
	echo "     docker exec -i -t <container_name> bash"
	echo "and use the shell to understand what happened!!"
	sleep infinity
fi
