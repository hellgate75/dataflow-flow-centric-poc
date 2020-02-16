<p align="center"><image width="250" height="250" src="../images/flow-centric-poc-logo.png">&nbsp;<image width="120" height="80" src="../images/dataflow-logo.png"></image></p><br/>
<br/>
<p align="right"><img src="https://travis-ci.org/hellgate75/flow-centric-poc.svg?branch=master" alt="trevis-ci" width="98" height="20" />&nbsp;<a href="https://travis-ci.org/hellgate75/flow-centric-poc">Check last build on Travis-CI</a></p><br/>
<br/>

# Flow-Centric PoC - Local Integration tests

It requires the installation of the following products:

* RabbitMQ (in the docker source folder there is the definitions file you can import, with a post call at the path /api/definitions, and using the default installtion as security use guest:guest as user, good catch using curl)
* MongoDb
* H2 Database Server ( data at: [https://github.com/hellgate75/dataflow-flow-centric-config/data/flow-centric-poc-db.mv.db](https://github.com/hellgate75/dataflow-flow-centric-config/blob/master/data/flow-centric-poc-db.mv.db?raw=true) and it must be placed in the root of the program in a folder named database/D3/, then you have to copt h2.sh from the docker sources and place it into the binary folder, replacing the original)

Or simply download the sample and run the services, you will find a docker build procedure after the download of the dev environment (preconfigured with schemas and data). 

You will need to add in the [env](env) file and add some attributes override from the Spring Cloud Config Server customization parameters, as shown in the following samples :


#### MongoDb
* --spring.data.mongodb.host=localhost
* --spring.data.mongodb.port=27017
* --spring.data.mongodb.username=
* --spring.data.mongodb.password=


#### Rabbit MQ
* --spring.rabbitmq.port=5672
* --spring.rabbitmq.host=localhost
* --spring.rabbitmq.username=fcapp
* --spring.rabbitmq.password=fcapp

#### H2 Database
* --"spring.dataprocess.url=jdbc:h2:tcp://169.254.22.214:65123/./flow-centric-poc-db;AUTO_RECONNECT=TRUE"
* --spring.dataprocess.username=sa
* --spring.dataprocess.password=sa




## Local test scripts


Local scripts are used for a simulation pre-release.

A build command has been provided into the folder, please run it as follow : 

```
../dataflow-flow-centric-local-runs> build-all.sh
```


Following scripts to simulate locally service after a build:

* start-config-server.sh -&gt; Will start pre-built Spring Cloud Config Server

* start-dataflow-source.sh -&gt; Will start pre-built Spring Dataflow Source Microservice

* start-dataflow-process.sh -&gt; Will start pre-built Spring Dataflow Processor Microservice

* start-dataflow-sink.sh* -&gt; Will start pre-built Spring Dataflow Sink Microservice



In order to start services, please follow the given sequence. Before to start microservices, please wait for the config server to start being running, looking at the command window.

In order to start services, please follow the given sample:


```
../dataflow-flow-centric-local-runs> start start-config-server.sh
```

This command will open a new command shell within the logs of the startup. Closing the shell you will stop the service.





## License

The library is licensed with [LGPL v. 3.0](/LICENSE) clauses, with prior authorization of author before any production or commercial use. Use of this library or any extension is prohibited due to high risk of damages due to improper use. No warranty is provided for improper or unauthorized use of this library or any implementation.

Any request can be prompted to the author [Fabrizio Torelli](https://www.linkedin.com/in/fabriziotorelli) at the follwoing email address:

[hellgate75@gmail.com](mailto:hellgate75@gmail.com)


