#!/bin/bash
TEMP_DIR="/var/h2-database/temp"
CONTROL_FILE="/var/h2-database/bin/.git-completed"

$CONTROL_FILE
GIT_UPDATE_GUARD="1"
##H2_BROWSER: Start a browser connecting to the web server (1|0|true|false)
if [ "1" = "$H2_DATA_GIT_REFRESH" ] || [ "true" = "$H2_DATA_GIT_REFRESH" ]; then
   GIT_UPDATE_GUARD="0"
   echo "Enabled force to update database files from github repositories!!"
fi



if [ -e $CONTROL_FILE ] && [ "1" = "$GIT_UPDATE_GUARD" ]; then
   echo "Get of database files from github repositories aready performed, so skipping the feature now"
   exit 0
fi

mkdir -p $TEMP_DIR
chmod 666 $TEMP_DIR
PWD="$(pwd)"
ENV="D3"
#H2_ENV: Environment folder for the database (default: D3)
if [ "" != "$H2_ENV" ] && [ "none" != "$H2_ENV" ]; then
	ENV="$1"
fi

#H2_DATA_GIT_URL: Stop the TCP server; example: ssh://git@github/com/{user}/{repo_name}.git|{folder|/};ssh://git@github/com/{user}/{repo_name}.git|{folder|/};...
if [ "" != "$H2_DATA_GIT_URL" ] && [ "none" != "$H2_DATA_GIT_URL" ]; then
	echo "Creating data folder: /var/h2-database/data/$ENV ..."
	mkdir -p /var/h2-database/data/$ENV

	IFS=";";for keypair in $H2_DATA_GIT_URL; do
	    REPO=""
		FOLDER=""
		IFS="|";for key in $keypair; do
			if [ "" != "$key" ]; then
				if [ "" = "$REPO" ]; then
					REPO=$key
				elif [ "" = "$FOLDER" ]; then
					FOLDER=$key
				fi
			fi
		done
		## Setting default folder path
		if [ "" = "$FOLDER" ]; then
			FOLDER="/"
		fi
		if [ "" != "$REPO" ] && [ "" != "$FOLDER" ]; then
			echo "Pulling repository $REPO using data folder: $FOLDER"
			cd $TEMP_DIR
			ROOT_FOLDER_NAME="$(echo "$REPO"|awk 'BEGIN {FS=OFS="/"}{print $NF}'|awk 'BEGIN {FS=OFS="."}{print $1}')"
			echo "Cloning Repository : $REPO into folder: $TEMP_DIR/$ROOT_FOLDER_NAME ..."
			git clone $REPO $TEMP_DIR/$ROOT_FOLDER_NAME
			XRES="$?"
			if [ "0" = "$XRES" ]; then
				echo "Repository : $REPO cloned successfully!!"
				echo "Changing to folder: $ROOT_FOLDER_NAME$FOLDER"
				if [ -e $TEMP_DIR/$ROOT_FOLDER_NAME$FOLDER ]; then
					cd $TEMP_DIR/$ROOT_FOLDER_NAME$FOLDER
					DB_LIST="$(ls *.db)"
					IFS=" ";for db_file in $DB_LIST; do
						echo "Copying database: $db_file in data folder: /var/h2-database/data/$ENV"
						cp $TEMP_DIR/$ROOT_FOLDER_NAME$FOLDER/$db_file /var/h2-database/data/$ENV/
					done
				else 
					echo "Folder $FOLDER doesn't exists in Repository: $REPO"
				fi
			else
				echo "Errors during repository $REPO clone operations -> exit code $XRES"
			fi
			cd $TEMP_DIR
			if [ -e $TEMP_DIR/$ROOT_FOLDER_NAME ]; then
				rm -Rf $TEMP_DIR/$ROOT_FOLDER_NAME
			fi
		else
			echo "Wrong Repository/Folder key-pair -> unparsed: $key key-pair"
		fi
	done
else
	echo "No github repository for data base files fetch required. Feature is disabled."
fi
touch $CONTROL_FILE
cd $PWD
rm -Rf $TEMP_DIR
echo "Use following RSA public key to allow access to your repositories in future use :"
cat ~/.ssh/id_rsa.pub
