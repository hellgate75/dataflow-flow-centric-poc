#!/bin/sh -e
sh ./defaults.sh
RES="$(sh ./precoditions.sh)"
if [[ "$RES" == "false" ]]; then
  exit 1
fi

if [[ "$BASE_DIR" == "" ]]; then
    BASE_DIR="$(pwd)"
fi
source "$BASE_DIR/configuration/env.sh"

function printDownloadFailure() {
  echo -e "Error during download : $1 package download was not completed, please re-run download procedure\n"
  echo -e  "Installation Procedure can not continue!!\n"
}
function printUnsuitableOSDownloadFailure() {
  echo -e "Unsuitable OS: $1 has not been downloaded due probably to Linux/Aix Kernel\n"
  echo -e  "Installation Procedure cannot continue!!\n"
}

function checkFileExistance() {
	LS="$(ls $1 2>/dev/null)"
	if [[ "$LS" == "" ]]; then
		echo "N"
	else
	    echo "Y"
	fi
}

echo "Checking installation files in download folder: $OUTPUT_FOLDER ..."

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/kafka_*.tgz")" == "N" ]]; then
  echo -e "$(printDownloadFailure "Apache Kafka")"
  exit 1
fi

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/MONGODB.zip")" == "N" ]]; then
  echo -e "$(printUnsuitableOSDownloadFailure "Mongo Db")"
  exit 1
fi

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/h2-database.zip")" == "N" ]]; then
  echo -e "$(printDownloadFailure "H2 Database")"
  exit 1
fi

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/spring-cloud-dataflow-server-*.jar")" == "N" ]]; then
  echo -e "$(printDownloadFailure "Spring Cloud Dataflow Server")"
  exit 1
fi

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/spring-cloud-dataflow-shell-*.jar")" == "N" ]]; then
  echo -e "$(printDownloadFailure "Spring Cloud Dataflow Shell")"
  exit 1
fi

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/spring-cloud-skipper-server-*.jar")" == "N" ]]; then
  echo -e "$(printDownloadFailure "Spring Cloud Skipper Server")"
  exit 1
fi

if [[ "$(checkFileExistance "$OUTPUT_FOLDER/spring-cloud-skipper-shell-*.jar")" == "N" ]]; then
  echo -e "$(printDownloadFailure "Spring Cloud Skipper Shell")"
  exit 1
fi


if [[ -e $SERVICES_FOLDER ]]; then
  echo "Removing previous installation files ..."
  rm -Rf $SERVICES_FOLDER
fi

echo "Create service folder: $SERVICES_FOLDER"
mkdir -p $SERVICES_FOLDER

#echo "Build maven project ..."
#sh $INSTALL_SCRIPTS_FOLDER/build-maven-project.sh
#RES="$?"
#if [[ "$RES" == "0" ]]; then
#	echo "Maven Build successful!!"
#else
#	echo "Errors occured during Maven Build!!"
#fi

echo "Preparing Spring DataFlow Server / Shell product for execution ..."
mkdir -p $SERVICES_FOLDER/spring-cloud-dataflow/bin
mkdir -p $SERVICES_FOLDER/spring-cloud-dataflow/jars
cp -f $OUTPUT_FOLDER/spring-cloud-dataflow-* $SERVICES_FOLDER/spring-cloud-dataflow/jars
#mv $OUTPUT_FOLDER/spring-cloud-dataflow-* $SERVICES_FOLDER/spring-cloud-dataflow/jars
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during copy/move of jars for Spring DataFlow Server / Shell"
	exit 1
fi
cp -f $APP_SCRIPTS_FOLDER/*-dataflow-* $SERVICES_FOLDER/spring-cloud-dataflow/bin
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during copy of shell scripts for Spring DataFlow Server / Shell"
	exit 1
fi
echo "Copying patches for Spring DataFlow Server / Shell ..."
echo "#!/bin/bash" > $SERVICES_FOLDER/spring-cloud-dataflow/bin/flow-centric-env.sh
echo "export BASE_DIR=\"$BASE_DIR\"" >> $SERVICES_FOLDER/spring-cloud-dataflow/bin/flow-centric-env.sh
echo "export DATAFLOW_DIR=\"$SERVICES_FOLDER/spring-cloud-dataflow\"" >> $SERVICES_FOLDER/spring-cloud-dataflow/bin/flow-centric-env.sh
chmod 777 $SERVICES_FOLDER/spring-cloud-dataflow/bin/flow-centric-env.sh

echo "Preparing Spring Skipper Server / Shell product for execution ..."
mkdir -p $SERVICES_FOLDER/spring-cloud-skipper/bin
mkdir -p $SERVICES_FOLDER/spring-cloud-skipper/jars
cp -f $OUTPUT_FOLDER/spring-cloud-skipper-* $SERVICES_FOLDER/spring-cloud-skipper/jars
#mv $OUTPUT_FOLDER/spring-cloud-skipper-* $SERVICES_FOLDER/spring-cloud-skipper/jars
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during copy/move of jars for Spring Skipper Server / Shell"
	exit 1
fi
cp -f $APP_SCRIPTS_FOLDER/*-skipper-* $SERVICES_FOLDER/spring-cloud-skipper/bin
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during copy of shell scripts for Spring Skipper Server / Shell"
	exit 1
fi
echo "Copying patches for Spring Skipper Server / Shell ..."
echo "#!/bin/bash" > $SERVICES_FOLDER/spring-cloud-skipper/bin/flow-centric-env.sh
echo "export BASE_DIR=\"$BASE_DIR\"" >> $SERVICES_FOLDER/spring-cloud-skipper/bin/flow-centric-env.sh
echo "export SKIPPER_DIR=\"$SERVICES_FOLDER/spring-cloud-skipper\"" >> $SERVICES_FOLDER/spring-cloud-skipper/bin/flow-centric-env.sh
chmod 777 $SERVICES_FOLDER/spring-cloud-skipper/bin/flow-centric-env.sh


echo "Preparing Apahe Kafka binaries ..."
mkdir -p $SERVICES_FOLDER/apache-kafka
cp -f $OUTPUT_FOLDER/kafka_* $SERVICES_FOLDER/apache-kafka
#mv $OUTPUT_FOLDER/kafka_* $SERVICES_FOLDER/apache-kafka
tar -xzf $SERVICES_FOLDER/apache-kafka/kafka_* -C $SERVICES_FOLDER/apache-kafka
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during extraction of Apache Kafka binaries"
	exit 1
fi
rm -f $SERVICES_FOLDER/apache-kafka/kafka_*.tgz
mv $SERVICES_FOLDER/apache-kafka/kafka_*/* $SERVICES_FOLDER/apache-kafka/
rm -rf $SERVICES_FOLDER/apache-kafka/kafka_*
echo "Copying patches for Apache Kafka ..."
cp $BASE_DIR/patches/apache-kafka/* $SERVICES_FOLDER/apache-kafka/bin
cp $BASE_DIR/patches/apache-kafka-config/* $SERVICES_FOLDER/apache-kafka/config
mkdir $SERVICES_FOLDER/apache-kafka/tmp
chmod 777 $SERVICES_FOLDER/apache-kafka/bin/*.sh
echo "#!/bin/bash" > $SERVICES_FOLDER/apache-kafka/bin/flow-centric-env.sh
echo "export BASE_DIR=\"$BASE_DIR\"" >> $SERVICES_FOLDER/apache-kafka/bin/flow-centric-env.sh
echo "export KAFKA_DIR=\"$SERVICES_FOLDER/apache-kafka\"" >> $SERVICES_FOLDER/apache-kafka/bin/flow-centric-env.sh
chmod 777 $SERVICES_FOLDER/apache-kafka/bin/flow-centric-env.sh

echo "Preparing H2 database binaries ..."
mkdir -p $SERVICES_FOLDER/h2-database
cp -f $OUTPUT_FOLDER/h2-database.zip $SERVICES_FOLDER/h2-database
#mv $OUTPUT_FOLDER/h2-database.zip $SERVICES_FOLDER/h2-database
unzip -q $SERVICES_FOLDER/h2-database/h2-database.zip -d $SERVICES_FOLDER/h2-database
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during extraction of H2 database binaries"
	exit 1
fi
rm -f $SERVICES_FOLDER/h2-database/h2-database.zip
mv $SERVICES_FOLDER/h2-database/h2/* $SERVICES_FOLDER/h2-database/
rm -rf $SERVICES_FOLDER/h2-database/h2
echo "Copying patches for H2 database ..."
cp $BASE_DIR/patches/h2-database/* $SERVICES_FOLDER/h2-database/bin
chmod 777 $SERVICES_FOLDER/h2-database/bin/*.sh
echo "#!/bin/bash" > $SERVICES_FOLDER/h2-database/bin/flow-centric-env.sh
echo "export BASE_DIR=\"$BASE_DIR\"" >> $SERVICES_FOLDER/h2-database/bin/flow-centric-env.sh
echo "export H2_DIR=\"$SERVICES_FOLDER/h2-database\"" >> $SERVICES_FOLDER/h2-database/bin/flow-centric-env.sh
chmod 777 $SERVICES_FOLDER/h2-database/bin/flow-centric-env.sh

echo "Preparing Mongo NoSQL Database binaries ..."
mkdir -p $SERVICES_FOLDER/mongodb
cp -f $OUTPUT_FOLDER/MONGODB.zip $SERVICES_FOLDER/mongodb
#mv $OUTPUT_FOLDER/MONGODB.zip $SERVICES_FOLDER/mongodb
unzip -q $SERVICES_FOLDER/mongodb/MONGODB.zip -d $SERVICES_FOLDER/mongodb
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during extraction of Mongo Database binaries"
	exit 1
fi
rm -f $SERVICES_FOLDER/mongodb/MONGODB.zip
mv $SERVICES_FOLDER/mongodb/mongodb-*/* $SERVICES_FOLDER/mongodb/
rm -rf $SERVICES_FOLDER/mongodb/mongodb-*
echo "Copying patches for Mongo NoSQL Database ..."
echo "#!/bin/bash" > $SERVICES_FOLDER/mongodb/bin/flow-centric-env.sh
echo "export BASE_DIR=\"$BASE_DIR\"" >> $SERVICES_FOLDER/mongodb/bin/flow-centric-env.sh
echo "export MONGODB_DIR=\"$SERVICES_FOLDER/mongodb\"" >> $SERVICES_FOLDER/mongodb/bin/flow-centric-env.sh
chmod 777 $SERVICES_FOLDER/mongodb/bin/flow-centric-env.sh


echo "Preparing Flow Centric Spring Cloud Config Server binaries ..."
mkdir -p $SERVICES_FOLDER/spring-cloud-config-server/bin
mkdir -p $SERVICES_FOLDER/spring-cloud-config-server/jars
cp -f $BASE_DIR/../dataflow-ms-flow-centric-config-server/target/dataflow-ms-flow-centric-config-server-*.jar $SERVICES_FOLDER/spring-cloud-config-server/jars/dataflow-ms-flow-centric-config-server.jar
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during copy of jars for Flow Centric Spring Cloud Config Server"
	exit 1
fi
cp -f $APP_SCRIPTS_FOLDER/*-config-server.sh $SERVICES_FOLDER/spring-cloud-config-server/bin
RES="$?"
if [[ "$RES" != "0" ]]; then
	echo "Errors during copy of shell scripts for Spring Skipper Server / Shell"
	exit 1
fi
echo "Copying patches for Flow Centric Spring Cloud Config Server ..."
echo "#!/bin/bash" > $SERVICES_FOLDER/spring-cloud-config-server/bin/flow-centric-env.sh
echo "export BASE_DIR=\"$BASE_DIR\"" >> $SERVICES_FOLDER/spring-cloud-config-server/bin/flow-centric-env.sh
echo "export CONFIG_SERVER_DIR=\"$SERVICES_FOLDER/spring-cloud-config-server\"" >> $SERVICES_FOLDER/spring-cloud-config-server/bin/flow-centric-env.sh
chmod 777 $SERVICES_FOLDER/spring-cloud-config-server/bin/flow-centric-env.sh

exit 0

