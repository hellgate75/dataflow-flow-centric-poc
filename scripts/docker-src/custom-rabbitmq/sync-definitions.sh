#!/bin/bash

PRODUCT_NAME="$1"
SYNC_FILE_FOLDER="$2"
SYNC_FILE_NAME="$3"
SYNC_SCRIPTS_FOLDER="$4"
SYNC_CUSTOM_URL="$5"

if [ -e $SYNC_SCRIPTS_FOLDER/.definition-sync-ok ]; then
        echo "RabbitMQ Definitions Sync already ran on this machine!!"
        exit 0
fi

echo "Start RabbitMQ Definitions Sync ..."

while [ "" = "$(netstat -an 2> /dev/null | grep 15672)" ]; do
        sleep 2
done

curl
if [ -e $SYNC_FILE_FOLDER/$SYNC_FILE_NAME ]; then
        echo "Sync of api definition for product: $PRODUCT_NAME"
        URL="http://localhost:15672/api/definitions"
        if [ "" != "$SYNC_CUSTOM_URL" ]; then
                URL="$SYNC_CUSTOM_URL"
        fi
        echo "Using definitions access url: $URL"
        echo "Using Json definitions input file: $SYNC_FILE_FOLDER/$SYNC_FILE_NAME"
        cd $SYNC_FILE_FOLDER
         CMD="curl -u guest:guest -X POST -H \"Content-Type: application/json\"  -H \"Accept: application/json\" -d '$(cat $SYNC_FILE_FOLDER/$SYNC_FILE_NAME)' $URL"
	 echo "Running POST call : $CMD"
	 eval "$CMD"
         rm -f $SYNC_FILE_FOLDER/$SYNC_FILE_NAME
         echo "Tracking file presence on file: $SYNC_SCRIPTS_FOLDER/.definition-sync-ok"
         touch $SYNC_SCRIPTS_FOLDER/.definition-sync-ok
else
        echo "Unable to locate Json definitions file: $SYNC_FILE_FOLDER/$SYNC_FILE_NAME"
        exit 1
fi

echo "RabbitMQ Definitions Sync complete!!"
exit 0
