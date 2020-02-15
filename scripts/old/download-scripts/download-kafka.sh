#!/bin/sh -e

if [[ "$BASE_DIR" == "" ]]; then
    BASE_DIR="$(pwd)"
fi
source "$BASE_DIR/configuration/env.sh"

if [[ ! -e $OUTFOLDER ]]; then
   echo "Creating output folder: $OUTFOLDER"
   mkdir -p $OUTFOLDER
fi

FILENAME="kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz"

url=$(curl --stderr /dev/null "https://www.apache.org/dyn/closer.cgi?path=/kafka/${KAFKA_VERSION}/${FILENAME}&as_json=1" | jq -r '"\(.preferred)\(.path_info)"')

echo ""
echo "Verify URL for Karka (Scala v. $SCALA_VERSION) version $KAFKA_VERSION ..."

# Test to see if the suggested mirror has this version, currently pre 2.1.1 versions
# do not appear to be actively mirrored. This may also be useful if closer.cgi is down.
if [[ ! $(curl -s -f -I "${url}") ]]; then
    echo "Mirror does not have desired version, downloading direct from Apache"
    url="https://archive.apache.org/dist/kafka/${KAFKA_VERSION}/${FILENAME}"
fi

echo ""
echo "Downloading Apache Kafka (Scala v. $SCALA_VERSION) version $KAFKA_VERSION from $url ..."
wget "${url}" -O "$OUTFOLDER/${FILENAME}"
#RES="$?"
RES="0"
CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/${FILENAME}" "$RES" "Apache Kafka (Scala v. $SCALA_VERSION) version $KAFKA_VERSION")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
exit 0
