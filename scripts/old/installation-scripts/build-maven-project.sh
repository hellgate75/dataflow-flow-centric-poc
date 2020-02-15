#!/bin/sh -e
MAVEN="$(which mvn)"
if [[ "" == "$MAVEN" ]]; then
	MAVEN="$(ls ~/apps/|grep apache-maven)"
	if [[ "" != "$MAVEN" ]]; then
		echo "Found maven path '~/apps/$MAVEN/bin'"
		MAVEN="$(ls ~/apps/$MAVEN/bin/mvn)"
	else
		echo "Couldn't find maven in your system..."
		exit 1
	fi
fi
DIR="$(pwd)"
cd ..
$MAVEN -U -up clean install
RES="$?"
cd $DIR
if [[ "$RES" != "0" ]]; then
   exit $RES
fi

exit 0
