#!/bin/sh
echo As first build the project using script:
echo ./build-maven-project.sh
echo 
echo start local RabbitMQ instance
echo start Merge Layer H2 local database from module folder database backup using h2.bat into bin folder after unzip of provided archive,
echo 
echo Run command(s):
echo Run swerver using following commands:
echo ./start-dataflow-skipper-server.sh
echo Wait for skipper server to start (check using using ./tail-skipper-server.sh)
echo ./start-dataflow-server.sh
echo Wait for spring dataflow server to start (check using using ./tail-dataflow-server.sh)
echo 
echo Services are using the config server. if you want use them run a local instance of config server using the procided script, after the maven buils.
echo config server start script :
echo run-config-server.sh
echo 
echo 
echo Run Spring Cloud DataFlow Shell :
echo ./start-dataflow-shell.sh
echo
echo
echo
echo You can run automation script as follow in the Spring Cloud DataFlow Shell :
echo script --file "./install-services-scdf-shell.script"  
echo
echo
echo 
echo Or eventually you can run the following script(s) in the sequence
echo 
echo Execute to register Source App as follow:
echo app register --name merge-layer-source --type source --uri maven://com.dataflow.merge.layer:dataflow-ms-merge-layer-source-poc:1.0.0.SNAPSHOT
echo
echo Execute to register Processor App as follow:
echo app register --name merge-layer-processor --type processor --uri maven://com.dataflow.merge.layer:dataflow-ms-merge-layer-process-poc:1.0.0.SNAPSHOT
echo
echo Execute to register Sink App as follow:
echo app register --name merge-layer-sink --type sink --uri maven://com.dataflow.merge.layer:dataflow-ms-merge-layer-sink-poc:1.0.0.SNAPSHOT
echo
echo
echo Streams:
echo
echo Source -> Processor -> Sink Flow Stream:
echo stream create --name merge-layer-rabbit-stream --definition "merge-layer-source | merge-layer-processor | merge-layer-sink" --description "Source Merge Layer stream" --deploy false
echo stream deploy --name merge-layer-rabbit-stream --packageVersion 1.0.0 --propertiesFile ./stream-config/stream-config.properties
echo
echo
echo Welcome to process server application
