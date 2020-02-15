#!/bin/bash

#(1)PRODUCT_NAME="Flow Centric RabbitMq Definitions"
#(2)SYNC_FILE_FOLDER=/root
#(3)SYNC_FILE_NAME=custom-definitions.json
#(4)SYNC_SCRIPTS_FOLDER=/root
#(5)SYNC_CUSTOM_URL=

chmod 660 /root/custom-definitions.json

CMD="$(echo "/root/sync-definitions.sh \"Flow Centric RabbitMq Definitions\" \"/root\" \"custom-definitions.json\" \"/root\"")"

echo "Starting Sync command: $CMD"

sh -c "$CMD" >> /root/definitions-sync.log &

sh -c "tail -f /root/definitions-sync.log" &