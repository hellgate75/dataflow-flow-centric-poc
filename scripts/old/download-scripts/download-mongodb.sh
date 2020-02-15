#!/bin/sh -e

if [[ "$BASE_DIR" == "" ]]; then
    BASE_DIR="$(pwd)"
fi
source "$BASE_DIR/configuration/env.sh"

OS="$(uname -s)"
OS_TYPE="L"
OS_X64="N"

echo "OS: $OS"

if [[ "$(beginswith "$OS" "MINGW")" == "Y" ]]; then
  if [[ "$(beginswith "$OS" "MINGW64")" == "Y" ]]; then
    OS_X64="Y"
  fi
  OS_TYPE="M"
elif [[ "$(beginswith "$OS" "WIN")" == "Y" ]]; then
  if [[ "$(beginswith "$OS" "MINGW64")" == "Y" ]]; then
    OS_X64="Y"
  fi
  OS_TYPE="W"
fi

if [[ ! -e $OUTFOLDER ]]; then
   echo "Creating output folder: $OUTFOLDER"
   mkdir -p $OUTFOLDER
fi

if [[ -e  $OUTFOLDER/MONGODB.zip ]]; then
   echo "Removing previous MongoDb download files"
   rm -f "$OUTFOLDER/MONGODB.zip"
fi

if [[ "$OS_TYPE" == "M" ]]; then
  echo "Running on a Pseudo Windows OS (Ming compiler for Windows)..."
  if [[ "$OS_X64" == "N" ]]; then
    echo "Trying to install 64-bit package over possible 32-bit OS ..."
  fi 
  wget https://fastdl.mongodb.org/$MONGODB_OSTYPE/mongodb-$MONGODB_OSTYPE-$MONGODB_DIST-$MONGODB_SUBREL-$MONGODB_RELEASE.zip -O "$OUTFOLDER/MONGODB.zip"
#  RES="$?"
  RES="0"
  CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/MONGODB.zip" "$RES" "MongoDb v. $MONGODB_RELEASE")"
  RES="$?"
  echo -e "$CHECKRES\n"
  if [[ "$RES" != "0" ]]; then
	exit $RES
  fi
  exit 0
elif [[ "$OS_TYPE" == "W"  ]]; then
  echo "Running on a Windows OS..."
  if [[ "$OS_X64" == "N" ]]; then
    echo "Trying to infear X64 package over possible 32 bits OS ..."
  fi 
  wget https://fastdl.mongodb.org/$MONGODB_OSTYPE/mongodb-$MONGODB_OSTYPE-$MONGODB_DIST-$MONGODB_SUBREL-$MONGODB_RELEASE.zip -O "$OUTFOLDER/MONGODB.zip"
#  RES="$?"
  RES="0"
  CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/MONGODB.zip" "$RES" "MongoDb v. $MONGODB_RELEASE")"
  RES="$?"
  echo -e "$CHECKRES\n"
  if [[ "$RES" != "0" ]]; then
	exit $RES
  fi
  exit 0
else
  echo "Running on Linux / Aix OS ..."
  echo "Feature not implemented ..."
fi
exit 1
