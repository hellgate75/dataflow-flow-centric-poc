#!/bin/sh -e

if [[ "$BASE_DIR" == "" ]]; then
    BASE_DIR="$(pwd)"
fi

eval $(cat "$BASE_DIR"/configuration/env.sh)

if [[ ! -e $OUTFOLDER ]]; then
   echo "Creating output folder: $OUTFOLDER"
   mkdir -p $OUTFOLDER
fi

if [[ -e  $OUTFOLDER/h2-database.zip ]]; then
   echo "Removing previous H2 Database download files"
   rm -f "$OUTFOLDER/h2-database.zip"
fi

wget https://h2database.com/h2-$H2_DB_RELEASE.zip -O "$OUTFOLDER/h2-database.zip"
#RES="$?"
RES="0"
CHECKRES="$(binaryDownloadCheck "$OUTFOLDER/h2-database.zip" "$RES" "H2 Database v. $H2_DB_RELEASE")"
RES="$?"
echo -e "$CHECKRES\n"
if [[ "$RES" != "0" ]]; then
	exit $RES
fi
exit 0
