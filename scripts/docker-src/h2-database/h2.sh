#!/bin/bash
ENV="D3"
ADMIN_PORT="9090"
TCP_PORT="65123"
PG_PORT="9023"

#H2_ENV: Environment folder for the database (default: D3)
if [ "" != "$H2_ENV" ] && [ "none" != "$H2_ENV" ]; then
	ENV="$1"
fi
##H2_WEB_PORT: Web Module Port (none|<value>, default: 9090)
if [ "" != "$H2_WEB_PORT" ] && [ "none" != "$H2_WEB_PORT" ]; then
	ADMIN_PORT="$H2_WEB_PORT"
fi
##H2_TCP_PORT: TCP Database Port (none|<value>, default: 65123)
if [ "" != "$H2_TCP_PORT" ] && [ "none" != "$H2_TCP_PORT" ]; then
	TCP_PORT="$H2_TCP_PORT"
fi
##H2_PG_PORT: PG Module Port (none|<value>, default: 9023)
if [ "" != "$H2_PG_PORT" ] && [ "none" != "$H2_PG_PORT" ]; then
	PG_PORT="$H2_PG_PORT"
fi

echo "Environment: $ENV"
echo "Web Console Port: $ADMIN_PORT"
echo "Database Port: $TCP_PORT"
echo "PG Port: $PG_PORT"
echo "Database folder: /var/h2-database/data/$ENV"

TCP_ARGS="-tcp -tcpPort $TCP_PORT -tcpAllowOthers"
WEB_ARGS="-web -webPort $ADMIN_PORT -webAllowOthers"
PG_ARGS="-pg -pgPort $PG_PORT -pgAllowOthers -pgDaemon"
INSECURE_ARGS="-ifNotExists"
SECURE_ARGS="-ifExists"
OTHERS=""

##H2_ENABLE_WEB: Enable Web feature (1|0|true|false)
if [ "1" != "$H2_ENABLE_WEB" ] && [ "true" != "$H2_ENABLE_WEB" ]; then
   WEB_ARGS=""
   echo "Disabled Web Console"
else
   echo "Enabled Web Console"
fi
##H2_ENABLE_PG: Enable PG feature (1|0|true|false)
if [ "1" != "$H2_ENABLE_PG" ] && [ "true" != "$H2_ENABLE_PG" ]; then
   PG_ARGS=""
   echo "Disabled PG Port"
else
   echo "Enabled PG Port"
fi
##H2_ENABLE_INSECURE: Databases are created when accessed (1|0|true|false)
if [ "1" != "$H2_ENABLE_INSECURE" ] && [ "true" != "$H2_ENABLE_INSECURE" ]; then
	INSECURE_ARGS=""
   echo "Disabled Insecure Remote Database creation"
else
   echo "Enabled Insecure Remote Database creation"
fi
##H2_ENABLE_SECURE: Only existing databases may be opened (1|0|all servers)
if [ "1" != "$H2_ENABLE_SECURE" ] && [ "true" != "$H2_ENABLE_SECURE" ]; then
	SECURE_ARGS=""
   echo "Disabled Secure Server -> No Remote Database creation"
else
   INSECURE_ARGS=""
   echo "Enabled Secure Server -> No Remote Database creation"
   echo "Disabled Insecure Remote Database creation"
fi

##H2_BROWSER: Start a browser connecting to the web server (1|0|true|false)
if [ "1" = "$H2_BROWSER" ] || [ "true" = "$H2_BROWSER" ]; then
   OTHERS="$OTHERS -browser"
   echo "Defined Server capability: BROWSER"
fi
##H2_ENABLE_DAEMONS: Enable daemons sample(none|web,pg,tcp)
if [ "" != "$H2_ENABLE_DAEMONS" ] && [ "none" != "$H2_ENABLE_DAEMONS" ]; then
   if [ "" != "$(echo "$H2_ENABLE_DAEMONS"| grep web)" ]; then
   	  OTHERS="$OTHERS -webDaemon"
   fi
   if [ "" != "$(echo "$H2_ENABLE_DAEMONS"| grep tcp)" ]; then
   	  OTHERS="$OTHERS -tcpDaemon"
   fi
   if [ "" != "$(echo "$H2_ENABLE_DAEMONS"| grep pg)" ]; then
   	  OTHERS="$OTHERS -pgDaemon"
   fi
   echo "Defined Server capability: DAEMONS => $H2_ENABLE_DAEMONS"
fi
##H2_PROPS_FOLDER: Server properties (default: ~, disable: null)
if [ "" != "$H2_ENABLE_DAEMONS" ] && [ "none" != "$H2_ENABLE_DAEMONS" ]; then
	OTHERS="$OTHERS -properties \"$H2_ENABLE_DAEMONS\""
fi
##H2_ENABLE_WEB_SSL: Use encrypted (HTTPS) connections (1|0|true|false)
if [ "1" = "$H2_ENABLE_WEB_SSL" ] || [ "true" = "$H2_ENABLE_WEB_SSL" ]; then
   OTHERS="$OTHERS -webSSL"
   echo "Defined Server capability: WEB SSL"
fi
##H2_ENABLE_TCP_SSL: Use encrypted (SSL) connections
if [ "1" = "$H2_ENABLE_TCP_SSL" ] || [ "true" = "$H2_ENABLE_TCP_SSL" ]; then
   OTHERS="$OTHERS -tcpSSL"
   echo "Defined Server capability: TCP SSL"
fi
##H2_ENABLE_WEB_PASSWORD: Enable Password of DB Console administrator (1|0|true|false)
if [ "1" = "$H2_ENABLE_WEB_PASSWORD" ] || [ "true" = "$H2_ENABLE_WEB_PASSWORD" ]; then
	##H2_WEB_PASSWORD: The Password of DB Console (none|<value>)
	#ENV H2_WEB_PASSWORD			none
	OTHERS="$OTHERS -webAdminPassword"
fi
##H2_ENABLE_TCP_PASSWORD: Enable Password for shutting down a TCP server (1|0|true|false)
if [ "1" = "$H2_ENABLE_TCP_PASSWORD" ] || [ "true" = "$H2_ENABLE_TCP_PASSWORD" ]; then
	##H2_TCP_PASSWORD: The password for shutting down a TCP server (none|<value>)
	if [ "" != "$H2_TCP_PASSWORD" ] && [ "none" != "$H2_TCP_PASSWORD" ]; then
		OTHERS="$OTHERS -tcpPassword \"$H2_TCP_PASSWORD\""
	fi
	
fi
##H2_TRACE: Enable Print additional trace information (all servers)  (1|0|true|false)
if [ "1" = "$H2_TRACE" ] || [ "true" = "$H2_TRACE" ]; then
	OTHERS="$OTHERS -trace"
fi
##H2_FORCE_SHUTDOWN: Do not wait until all connections are closed (1|0|true|false)
if [ "1" = "$H2_FORCE_SHUTDOWN" ] || [ "true" = "$H2_FORCE_SHUTDOWN" ]; then
	OTHERS="$OTHERS -tcpShutdownForce"
fi
##H2_SHUTDOWN_URL: Stop the TCP server; example: tcp://localhost
if [ "" != "$H2_SHUTDOWN_URL" ] && [ "none" != "$H2_SHUTDOWN_URL" ]; then
	OTHERS="$OTHERS -tcpShutdown \"$H2_SHUTDOWN_URL\""
fi

sh /var/h2-database/bin/check-repo-datafiles.sh

java -cp /var/h2-database/bin/h2-1.4.*.jar org.h2.tools.Server $TCP_ARGS $WEB_ARGS $PG_ARGS $INSECURE_ARGS $SECURE_ARGS $SECURE_ARGS $OTHERS -baseDir /var/h2-database/data/$ENV $@ 
STATE="$?"
if [ "0" != "$STATE" ]; then
	echo "ERRORS Executing: java -cp /var/h2-database/bin/h2-1.4.*.jar org.h2.tools.Server $TCP_ARGS $WEB_ARGS $PG_ARGS $INSECURE_ARGS $SECURE_ARGS $SECURE_ARGS $OTHERS -baseDir /var/h2-database/data/$ENV $@"
fi
