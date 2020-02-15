#!/bin/sh
dir=$(dirname "$0")
ENV="D3"
if [[ "" != "$1" ]]; then
	ENV="$1"
fi
#java -cp "$dir/h2-1.4.199.jar:$H2DRIVERS:$CLASSPATH" org.h2.tools.Console -webPort 9090 "$@"
java -cp "$dir/h2-1.4.199.jar:$H2DRIVERS:$CLASSPATH" org.h2.tools.Console -tcp -tcpPort 65123 -tcpAllowOthers -web -webPort 9090 -webAllowOthers -baseDir /apps/opt/efsvdp/billprod-merge-layer/database/$ENV $@ 
