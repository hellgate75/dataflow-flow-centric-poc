#!/bin/sh -e
BASE_DIR="$(pwd)"

sh ./defaults.sh
RES="$(sh ./precoditions.sh)"
if [[ "$RES" == "false" ]]; then
  exit 1
fi

source $BASE_DIR/configuration/env.sh

KAFKA_FOLDER="$SERVICES_FOLDER/apache-kafka"
H2_FOLDER="$SERVICES_FOLDER/h2-database"
MONGODB_FOLDER="$SERVICES_FOLDER/mongodb"
DATAFLOW_FOLDER="$SERVICES_FOLDER/spring-cloud-dataflow"
SKIPPER_FOLDER="$SERVICES_FOLDER/spring-cloud-skipper"
CONFIG_SERVER_FOLDER="$SERVICES_FOLDER/spring-cloud-config-server"

function stopService() {
	FOLDER=$1
	STOP_SCRIPT=$2
	NAME=$3
	#echo "Stopping $NAME service ...\n"
	cd $FOLDER/bin
	sh -c "export BASE_DIR=$BASE_DIR;source ./flow-centric-env.sh;./$STOP_SCRIPT" *> echo
	RES="$?"
	if [[ "$RES" == "0" ]]; then
		echo -e "$NAME service stopped successfully!!\n"
		exit 0
	else
		echo -e "Errors during stop of $NAME service!!\n"
		exit 1
	fi
	cd $BASE_DIR
}

function stopZookeeperForApacheKafka() {
#	RES_TEXT="$(stopService "$KAFKA_FOLDER" "zookeeper-server-stop.sh" "Zookeeper for Apache Kafka" )"
#	RES="$?"
#	if [[ "$RES" == "0" ]]; then
#		echo -e "$RES_TEXT"
#		exit 0
#	else
#		echo -e "$RES_TEXT"
#		exit 1
#	fi
    PID="$(ls $KAFKA_FOLDER/logs/zookeeper.pid 2> /dev/null)"
	if [[ "$PID" == "" ]]; then
	   echo "Unable to find apache Zookeeper PID!!"
	   exit 1
	else
	   sh -c "kill -s 9 $PID"
	   echo "Apache Zookeeper (PID: $PID) Stopped!!"
	   exit 0
	fi
}
function stopApacheKafka() {
#	RES_TEXT="$(stopService "$KAFKA_FOLDER" "kafka-server-stop.sh" "Apache Kafka" )"
#	RES="$?"
#	if [[ "$RES" == "0" ]]; then
#		echo -e "$RES_TEXT"
#		exit 0
#	else
#		echo -e "$RES_TEXT"
#		exit 1
#	fi
    PID="$(ls $KAFKA_FOLDER/logs/kafkaServer.pid 2> /dev/null)"
	if [[ "$PID" == "" ]]; then
	   echo "Unable to find apache Kafka PID!!"
	   exit 1
	else
	   sh -c "kill -s 9 $PID"
	   echo "Apache Kafka (PID: $PID) Stopped!!"
	   exit 0
	fi
}
echo "Stopping services ..."

echo ""
echo ""
echo "Stopping Apache Kafka group services"
echo ""
echo "Stopping Apache Zookeper for Apache Kafka service ..."
echo -e "$(stopZookeeperForApacheKafka)"
echo ""
echo "Stopping Apache Kafka service ..."
echo -e "$(stopApacheKafka)"
echo ""
echo ""


echo "Services stopped successful!!" 
sleep 2
apps-scripts/check-state-by-port.sh
