#!/bin/sh -e

if [[ "$BASE_DIR" == "" ]]; then
    BASE_DIR="$(pwd)"
fi
source "$BASE_DIR/configuration/env.sh"

if [[ ! -e $OUTFOLDER ]]; then
   echo "Creating output folder: $OUTFOLDER"
   mkdir -p $OUTFOLDER
fi

echo ""
#Download Spring Data Flow binaries
echo "Download Spring Data Flow binaries..."

if [[ "" != "$1" ]]; then
SCDF_RELEASE="$1"
fi
if [[ "" != "$2" ]]; then
SKIPPER_RELEASE="$2"
fi
echo ""
echo "Downloading server packages for spring cloud data flow v. $SCDF_RELEASE"
echo ""
echo "Downloading executables for Spring Cloud Data Flow Server v. $SCDF_RELEASE ..."
wget https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-dataflow-server/$SCDF_RELEASE/spring-cloud-dataflow-server-$SCDF_RELEASE.jar -O $OUTFOLDER/spring-cloud-dataflow-server-$SCDF_RELEASE.jar
#RES="$?"
RES="0"
echo ""
CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/spring-cloud-dataflow-server-$SCDF_RELEASE.jar" "$RES" "Spring Cloud Data Flow Server v. $SCDF_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
echo "Downloading executables for Spring Cloud Data Flow Shell v. $SCDF_RELEASE ..."
wget https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-dataflow-shell/$SCDF_RELEASE/spring-cloud-dataflow-shell-$SCDF_RELEASE.jar -O $OUTFOLDER/spring-cloud-dataflow-shell-$SCDF_RELEASE.jar
#RES="$?"
RES="0"
echo ""
CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/spring-cloud-dataflow-shell-$SCDF_RELEASE.jar" "$RES" "Spring Cloud Data Flow Shell v. $SCDF_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
echo "Downloading executables for Spring Cloud Skipper Server v. $SKIPPER_RELEASE ..."
wget https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-skipper-server/$SKIPPER_RELEASE/spring-cloud-skipper-server-$SKIPPER_RELEASE.jar -O $OUTFOLDER/spring-cloud-skipper-server-$SKIPPER_RELEASE.jar
#RES="$?"
RES="0"
echo ""
CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/spring-cloud-skipper-server-$SKIPPER_RELEASE.jar" "$RES" "Spring Cloud Skipper Server v. $SKIPPER_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
wget https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-skipper-shell/$SKIPPER_RELEASE/spring-cloud-skipper-shell-$SKIPPER_RELEASE.jar -O $OUTFOLDER/spring-cloud-skipper-shell-$SKIPPER_RELEASE.jar
#RES="$?"
RES="0"
echo ""
CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/spring-cloud-skipper-shell-$SKIPPER_RELEASE.jar" "$RES" "Spring Cloud Skipper Shell v. $SKIPPER_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
echo ""
echo ""
exit 0