#!/bin/bash
/bin/bash /root/bootstrap-sync-definitions.sh
docker-entrypoint.sh rabbitmq-server
if [ $# -gt 0 ]; then
	docker-entrypoint.sh "$@"
fi
