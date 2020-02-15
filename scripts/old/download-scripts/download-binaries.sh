#!/bin/sh -e

if [[ "$BASE_DIR" == "" ]]; then
    BASE_DIR="$(pwd)"
fi
source "$BASE_DIR/configuration/env.sh"

DOWNLOAD_SCRIPTS_FOLDER="$BASE_DIR/download-scripts"

if [[ -e $OUTFOLDER ]]; then
   echo "Destroy existing temporary download folder: $OUTFOLDER"
   rm -Rf $OUTFOLDER
fi

echo "Create temporary download folder: $OUTFOLDER"
mkdir -p $OUTFOLDER

echo "Downloading services' binaries ..."
sh $DOWNLOAD_SCRIPTS_FOLDER/download-mongodb.sh
RES="$?"
echo ""
CHECKRES="$(binaryDownloadProcedureCheck "$RES" "MongoDb v. $MONGODB_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
echo ""
echo ""
sh $DOWNLOAD_SCRIPTS_FOLDER/download-kafka.sh
#RES="$?"
RES="0"
echo ""
CHECKRES="$(binaryDownloadProcedureCheck "$RES" "Apache Kafka (Scala v. $SCALA_VERSION) version $KAFKA_VERSION")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
echo ""
echo ""
sh $DOWNLOAD_SCRIPTS_FOLDER/download-h2-database.sh
#RES="$?"
RES="0"
echo ""
CHECKRES="$(binaryDownloadProcedureCheck "$RES" "H2 Database v. $H2_DB_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
echo ""
echo ""
sh $DOWNLOAD_SCRIPTS_FOLDER/download-spring-dataflow.sh
RES="$?"
echo ""
CHECKRES="$(binaryDownloadProcedureCheck "$RES" "Spring Cloud Data Flow Server/Shell v. $SCDF_RELEASE & Spring Cloud Skipper Server/Shell v. $SKIPPER_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
echo ""
echo ""
echo "Download of services' binaries complete!!"
exit 0