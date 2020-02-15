#!/bin/sh -e

sh ./defaults.sh
RES="$(sh ./precoditions.sh)"
if [[ "$RES" == "false" ]]; then
  exit 1
fi

BASE_DIR="$(pwd)"

source $BASE_DIR/configuration/env.sh

KAFKA_FOLDER="$SERVICES_FOLDER/apache-kafka"
H2_FOLDER="$SERVICES_FOLDER/h2-database"
MONGODB_FOLDER="$SERVICES_FOLDER/mongodb"
DATAFLOW_FOLDER="$SERVICES_FOLDER/spring-cloud-dataflow"
SKIPPER_FOLDER="$SERVICES_FOLDER/spring-cloud-skipper"
CONFIG_SERVER_FOLDER="$SERVICES_FOLDER/spring-cloud-config-server"

function startService() {
	FOLDER=$1
	START_UP_SCRIPT=$2
	NAME=$3
#	echo "Starting $NAME service ...\n"
	cd $FOLDER/bin
	#source $FOLDER/bin/flow-centric-env.sh
	sh -c "export BASE_DIR=$BASE_DIR;source ./flow-centric-env.sh;./$START_UP_SCRIPT"
	RES="$?"
#	echo -e "service start res: $RES\n"
	if [[ "$RES" == "0" ]]; then
		echo -e "$NAME service started successfully!!\n"
		cd $BASE_DIR
		exit 0
	else
		echo -e "Errors during start-up of $NAME service!!\n"
		cd $BASE_DIR
		exit 1
	fi
}

function startZookeeperForApacheKafka() {
	RES_TEXT="$(startService "$KAFKA_FOLDER" "zookeeper-server-start.sh -daemon $KAFKA_FOLDER/config/zookeeper.properties" "Zookeeper for Apache Kafka" )"
	RES="$?"
	echo -e "$RES_TEXT"
	if [[ "$RES" == "0" ]]; then
		exit 0
	else
		exit 1
	fi

}
function startApacheKafka() {
	RES_TEXT="$(startService "$KAFKA_FOLDER" "kafka-server-start.sh -daemon $KAFKA_FOLDER/config/server.properties" "Apache Kafka" )"
	RES="$?"
	echo -e "$RES_TEXT"
	if [[ "$RES" == "0" ]]; then
		exit 0
	else
		exit 1
	fi
}

echo "Starting services ..."

echo ""
echo ""
echo "Starting Apache Kafka group services"
echo ""
echo "Starting Apache Zookepeer for Apache Kafka service ..."
ZOOKEEPER_RES="$(startZookeeperForApacheKafka)"
RES="$?"
if [[ "$RES" == "0" ]]; then
	echo -e "$ZOOKEEPER_RES"
else
	echo -e "$ZOOKEEPER_RES"
	exit 1
fi
echo ""
echo "Starting Apache Kafka service ..."
KAFKA_RES="$(startApacheKafka)"
RES="$?"
if [[ "$RES" == "0" ]]; then
	echo -e "$KAFKA_RES"
else
	echo -e "$KAFKA_RES"
	exit 1
fi
echo ""
echo "Apache Kafka service started!!"
echo ""
echo ""


echo "Services started successful!!" 
sleep 2
apps-scripts/check-state-by-port.sh
