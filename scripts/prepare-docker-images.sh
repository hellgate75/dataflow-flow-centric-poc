#!/bin/sh

PWD="$(pwd)"
FOLDER="$(dirname "$(realpath "$0")")"

source "$FOLDER/docker-src/docker-vars.sh"

DOCKERHUB_USER=""
DOCKERHUB_TOKEN=""

if [[ "" != "$DOCKERHUB_USER" ]]; then
   if [[ "" != "$DOCKERHUB_TOKEN" ]]; then
		echo "Logging to Docker Hub, with given token / password"
		docker login -u $DOCKERHUB_USER -p $DOCKERHUB_TOKEN 2> /dev/null
   else
	   	echo "Logging to Docker Hub, please type or paste your token / password:"
		docker login -u $DOCKERHUB_USER --password-stdin
   fi
else
	if [[ "" != "$(which connect-docker-hub)" ]]; then
		connect-docker-hub
	fi
	if [[ "" != "$(which connect-docker-hub)" ]]; then
		connect-docker-hub
	fi
	if [[ "" != "$(which docker-hub-variables)" ]]; then
		source "$(which docker-hub-variables)"
	fi
	
fi

if [[ "" == "$(docker image ls | grep rabbitmq | grep $RABBITMQ_RELEASE)" ]]; then
   echo "Pulling RabbitMQ v. $RABBITMQ_RELEASE docker image"
   docker pull rabbitmq:$RABBITMQ_RELEASE
else
	echo "RabbitMQ v. $RABBITMQ_RELEASE docker image already present!!"
fi
# run sample of Rabbit MQ custom container:
# docker run -d --tty --rm -p 4369:4369 -p 5671:5671 -p 25672:25672 -p 9082:15672 -p 9672:5672 --name test-rabbitmq-2 rabbitmq:3.8-rc-management

if [[ "" == "$(docker image ls | grep mongo | grep $MONGO_RELEASE)" ]]; then
   echo "Pulling MongoDb v. $MONGO_RELEASE docker image"
   docker pull mongo:$MONGO_RELEASE
else
	echo "MongoDb v. $MONGO_RELEASE docker image already present!!"
fi
# run sample of Oracle JDK custom container:
# docker run -d --tty --rm -p 27017:27017 --name test-mongo-2 mongo:3.6.17-xenial
# Connection docker machine
# docker run -d --tty --rm --name test-connect-mongo-2 mongo:3.6.17-xenial\
#    mongo --host $(sh ./docker-machine-ip.sh):27017

RABBITMQ_FOLDER="$FOLDER/docker-src/custom-rabbitmq"
if [[ "" == "$(docker image ls | grep $DOCKERHUB_USER/rabbitmq | grep $RABBITMQ_RELEASE-flow-centric)" ]]; then
  echo "Creating Custom RabbitMQ v. $RABBITMQ_RELEASE-flow-centric docker image"
  cd $RABBITMQ_FOLDER
  sh ./create-custom-rabbitmq-docker-image.sh
  RES="$?"
  cd $PWD
  echo "Results: $RES"
  if [[ "0" != "$RES" ]] && [[ "" != "$DOCKERHUB_USER" ]]; then
     docker push $DOCKERHUB_USER/rabbitmq:$RABBITMQ_RELEASE-flow-centric
  fi
else
	echo "Custom RabbitMQ v. $RABBITMQ_RELEASE-flow-centric docker image already present!!"
fi
# run sample of Custom Rabbit MQ custom container:
# docker run -d --tty --rm -p 4369:4369 -p 5671:5671 -p 25672:25672 -p 9082:15672 -p 9672:5672 --name test-rabbitmq-2 rabbitmq:3.8-rc-management-flow-centric
cd "$PWD"




JDK_FOLDER="$FOLDER/docker-src/oracle-jdk-1.8"
if [[ "" == "$(docker image ls | grep $DOCKERHUB_USER/oracle-jdk8 | grep $JDK_VERSION)" ]]; then
  echo "Creating Oracle JDK v. $JDK_VERSION Ubuntu docker image"
  cd "$JDK_FOLDER"
  if [[ ! -e ./jdk-8u241-linux-x64.tar.gz ]]; then
	curl -L https://drive.google.com/open?id=1J1RF8I6IhVd3LhtPeRWHXmnSPAL2Sa5x -o ./jdk-8u241-linux-x64.tar.gz
  fi
  docker --debug image build --rm . -t $DOCKERHUB_USER/oracle-jdk8:$JDK_VERSION
  if [[ "" != "$DOCKERHUB_USER" ]]; then
     docker push $DOCKERHUB_USER/oracle-jdk8:$JDK_VERSION
  fi
else
	echo "Oracle JDK v. $JDK_VERSION Ubuntu docker image already present!!"
fi
# run sample of Oracle JDK custom container:
# docker run --interactive --tty --rm --name test-jdk-2 hellgate75/oracle-jdk8:1.8.241-x64
cd "$PWD"



H2D_FOLDER="$FOLDER/docker-src/h2-database"
if [[ "" == "$(docker image ls | grep $DOCKERHUB_USER/h2-database | grep $H2_DATABASE_RELEASE)" ]]; then
echo "Creating H2 Database v. $H2_DATABASE_RELEASE docker image"
  cd "$H2D_FOLDER"
  docker --debug image build --rm . -t $DOCKERHUB_USER/h2-database:$H2_DATABASE_RELEASE
  if [[ "" != "$DOCKERHUB_USER" ]]; then
     docker push $DOCKERHUB_USER/h2-database:$H2_DATABASE_RELEASE
  fi
else
	echo "H2 Database v. $H2_DATABASE_RELEASE docker image already present!!"
fi
# run sample of H2 custom container:
# docker run  -d --tty --rm -v h2_data_volume:/var/h2-database/data -p 9081:9090 -p 65123:65123 -e "H2_ENABLE_WEB=true" -e "H2_ENABLE_INSECURE=true" -e "H2_ENABLE_SECURE=false" -e "H2_DATA_GIT_URL=git@github.com:hellgate75/dataflow-flow-centric-config.git|/data" --name test-h2-2 hellgate75/h2-database:2019.10.14
# docker run -interactive --tty --rm -v h2_data_volume:/var/h2-database/data -p 9099:9090 -p 65124:65123 -e "H2_ENABLE_WEB=true" -e "H2_ENABLE_INSECURE=true" -e "H2_ENABLE_SECURE=false" -e "H2_DATA_GIT_URL=git@github.com:hellgate75/dataflow-flow-centric-config.git|/data" --name test-h2-3 hellgate75/h2-database:2019.10.14
cd "$PWD"


CONFIG_SERVER_FOLDER="$FOLDER/docker-src/config-server"
if [[ "" == "$(docker image ls | grep $DOCKERHUB_USER/spring-cloud-config-server | grep $CONFIG_SERVER_RELEASE)" ]]; then
  echo "Creating Spring Cloud Config Server for Flow Centric v. $CONFIG_SERVER_RELEASE docker image"
  cd "$JDK_FOLDER"
  cd $CONFIG_SERVER_FOLDER
  ./create-config-server-docker-image.sh
  RES="$?"
  cd $PWD
  echo "Results: $RES"
  if [[ "0" != "$RES" ]] && [[ "" != "$DOCKERHUB_USER" ]]; then
     docker push $DOCKERHUB_USER/spring-cloud-config-server:$CONFIG_SERVER_RELEASE
  fi
else
	echo "Spring Cloud Config Server for Flow Centric v. $CONFIG_SERVER_RELEASE docker image already present!!"
fi
# run sample of Config Server custom container:
# docker run -d --tty --rm -p 8888:8888 --name test-config-server-2 hellgate75/spring-cloud-config-server:1.0.0-flow-centric
cd "$PWD"



if [[ "" != "$DOCKERHUB_USER" ]]; then
   docker logout
else
	if [[ "" != "$(which disconnect-docker-hub)" ]]; then
		disconnect-docker-hub
	fi
fi
