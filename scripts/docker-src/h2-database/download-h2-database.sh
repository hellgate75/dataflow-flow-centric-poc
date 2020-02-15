#!/bin/bash

FOLDER="$(dirname "$(realpath "$0")")"
source "$FOLDER"/env.sh

chmod +x "$FOLDER"/*
chmod +x "$PATCHES_FOLDER"/*
echo "Download of Binary Database files for H2 Database v. $H2_DB_RELEASE in progress ..."

if [ ! -e $PRODUCT_FOLDER ]; then
   echo "Creating product folder: $PRODUCT_FOLDER"
   mkdir -p $PRODUCT_FOLDER
fi

if [ -e  $DOWNLOAD_FOLDER/h2-database.zip ]; then
   echo "Removing previous H2 Database download files"
   rm -f "$DOWNLOAD_FOLDER/h2-database.zip"
fi

wget https://h2database.com/h2-$H2_DB_RELEASE.zip -O "$DOWNLOAD_FOLDER"/h2-database.zip
#RES=0
if [ ! -e "$DOWNLOAD_FOLDER"/h2-database.zip ]; then
    echo "Binary Database files for H2 Database v. $H2_DB_RELEASE didn't complete."
    echo "Installation failed!!"
	exit 1
fi
echo "Binary Database files for H2 Database v. $H2_DB_RELEASE is completed successfully."
echo "Progressing with the installation ..."
exit 0
