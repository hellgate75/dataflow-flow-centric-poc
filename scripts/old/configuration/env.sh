#!/bin/sh -e
SCALA_VERSION=2.12
KAFKA_VERSION=2.4.0
SCDF_RELEASE="2.3.0.RELEASE"
SKIPPER_RELEASE="2.3.0.RELEASE"
MONGODB_RELEASE="4.2.3"
MONGODB_SUBREL="2012plus"
MONGODB_OSTYPE="win32"
MONGODB_DIST="x86_64"
H2_DB_RELEASE="2019-10-14"
OUTFOLDER=$BASE_DIR/tmp
OUTPUT_FOLDER=$BASE_DIR/tmp
SERVICES_FOLDER=$BASE_DIR/services
APP_SCRIPTS_FOLDER=$BASE_DIR/apps-scripts
INSTALL_SCRIPTS_FOLDER=$BASE_DIR/installation-scripts
CONFIGURATION_FOLDER=$BASE_DIR/configuration
export PATH=$PATH:$BASE_DIR/bin


beginswith() {
  case $1 in 
      "$2"*) echo "Y";; 
      *) echo "N";; 
   esac; 
}

function binaryDownloadCheck() {
    if [[ "$#" != "3" ]]; then
		echo -e "Errors in function: binaryDownloadCheck arguments: $# <> 3"
		exit 2
	fi
	FILE="$1"
	RES="$2"
	NAME="$3"
	sleep 1
	if [[ "$RES" != "0" ]]; then
		echo "$NAME binaries download errors: see shell for details!!"
		exit 1
	fi

	if [[ -e "$FILE" ]]; then
		echo "$NAME binaries download successful!!"
	else
		echo "$NAME binaries download failure: binary file $FILE doesn't exists!!"
	fi
	exit 0
}

function binaryDownloadProcedureCheck() {
    if [[ "$#" != "2" ]]; then
		echo -e "Errors in function: binaryDownloadProcedureCheck arguments: $# <> 2"
		exit 2
	fi
	RES="$1"
	NAME="$2"
	if [[ "$RES" != "0" ]]; then
		echo "$NAME package download errors: see shell for details!!"
		exit 1
	else
		echo "$NAME package download successful!!"
		exit 0
	fi
}
