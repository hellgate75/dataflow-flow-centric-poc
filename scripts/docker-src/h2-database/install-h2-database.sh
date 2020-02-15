#!/bin/bash

FOLDER="$(dirname "$(realpath "$0")")"
source "$FOLDER"/env.sh

echo "Extracting ssh keys ..."
unzip /root/ssh.zip -d /root
chmod 600 /root/.ssh/id_rsa*
rm -f /root/ssh.zip

echo "Installation of Binary Database files for H2 Database v. $H2_DB_RELEASE in progress ..."

echo "Preparing H2 database binaries ..."
mkdir -p $PRODUCT_FOLDER/h2-database
unzip -q $DOWNLOAD_FOLDER/h2-database.zip -d $PRODUCT_FOLDER/h2-database
RES="$?"
if [ "$RES" != "0" ]; then
	echo "Errors during extraction of H2 Database binaries"
	exit 1
fi
if [ ! -e $PRODUCT_FOLDER/h2-database/data ]; then
	echo "Preparing H2 database data folder ..."
	mkdir $PRODUCT_FOLDER/h2-database/data
	chmod 666 $PRODUCT_FOLDER/h2-database/data
fi
echo "Removing not necessary files ..."
rm -f $DOWNLOAD_FOLDER/h2-database.zip
echo "Moving database files to final folder not necessary files ..."
mv $PRODUCT_FOLDER/h2-database/h2/* $PRODUCT_FOLDER/h2-database/
rm -rf $PRODUCT_FOLDER/h2-database/h2
echo "Copying patches for H2 database ..."
cp $PATCHES_FOLDER/* $PRODUCT_FOLDER/h2-database/bin
chmod 777 $PRODUCT_FOLDER/h2-database/bin/*.sh
rm -f $PRODUCT_FOLDER/h2-database/bin/*.bat
git config --global user.name hellgate75
git config --global user.email hellgate75@gmail.com
git config --global remote.origin.fetch refs/heads/*:refs/remotes/origin/*
ssh-keyscan github.com >> /root/githubKey
ssh-keygen -lf /root/githubKey
cat /root/githubKey >> ~/.ssh/known_hosts
cat ~/.ssh/known_hosts
exit 0

