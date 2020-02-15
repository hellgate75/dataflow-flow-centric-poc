#!/bin/bash
source "$(which ms-env.sh)"
echo "Stopping $APP_DESCR ..."
echo "====================================================================="
echo ""
if [ -e /var/run/$APP_GROUP/$APP_NAME.pid ]; then
	PID="$(cat /var/run/$APP_GROUP/$APP_NAME.pid)"
	echo ""
	echo "Process Id: <$PID>"
	echo ""
	if [ "" != "$PID" ]; then
		kill "$PID"
		echo "$APP_DESCR: STOPPED!!"
	else
		echo "Server nas no valid PID: empty!! Nothing to do."
		echo "$APP_DESCR: STOP ABANDONED!!"
	fi
else
	echo "Server nas no PID file!! Nothing to do."
	echo "$APP_DESCR: STOP ABANDONED!!"
fi
#kill -n 9 $(ps -eaf|grep java|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}')  $(ps -eaf|grep tail|grep -v grep|awk 'BEGIN {FS=OFS=" "}{print $2}')