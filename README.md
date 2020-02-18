<p align="center"><image width="250" height="250" src="images/flow-centric-poc-logo.png">&nbsp;<image width="150" height="110" src="images/dataflow-logo.png"></image></p><br/>
<p align="center"><image width="288" height="119" src="images/flow-centric.png"></p><br/>
<p align="right"><img src="https://travis-ci.org/hellgate75/dataflow-flow-centric-poc.svg?branch=master" alt="trevis-ci" width="98" height="20" />&nbsp;<a href="https://travis-ci.org/hellgate75/dataflow-flow-centric-poc">Check last build on Travis-CI</a></p><br/>
<br/>

# Flow Centric PoC


Complex json structures coming from different sources? We want to fix your problem. Take a look to get inspired!!!



You have small but complex data, and you want easily get ready for any analytics tools with less burn down
and expenses possible. So we tried at the end the use of BSON library and the Spring Cloud Dataflow framework, for running a 'continuous streaming'...



## First level PoC development.

In a real world we should provide at least a similar architecture:


<p align="center"><image width="‭946‬" height="535" src="images/arch-001.PNG"></p><br/>


As a Poc we can start providing in a single Maven project, all we can provide it's just a small subset of the minimum viable architecture, as shown here:

<p align="center"><image width="‭946‬" height="532" src="images/arch-002.PNG"></p><br/>



## Results

In the MongoDb instance you can find the flow-centric database.

 
<p align="center"><image width="946" height="666" src="images/screen-004.PNG"></p><br/>



After a cycle of data streaming we have two MongDb collections per each category. 


 
<p align="center"><image width="946" height="668" src="images/screen-006.PNG"></p><br/>


Into data collections we can find some ready indexes:


 
<p align="center"><image width="946" height="671" src="images/screen-007.PNG"></p><br/>



Here how MongDb collections appears.

<p align="center"><image width="946" height="624" src="images/screen-001.PNG"></p><br/>

And into any of the data collections we have elements that track :

* metadata partition

* metadata document id

* index

* model name

All as shown in following images:

<p align="center"><image width="946" height="554" src="images/screen-002.PNG"></p><br/>




## Spring Cloud Configuration

The Spring Cloud Config server takes the configuration from a specific repository, as follows:

* [dataflow-flow-centric-config](https://github.com/hellgate75/dataflow-flow-centric-config)


It provides some profiles:

* dev (source_dev, process_dev, sink_dev)

* compose (source_compose, process_compose, sink_compose)

* kubernetes (source_kubernetes, process_kubernetes, sink_kubernetes)

* local (not ready) 



## Test locally without building the code


We provide a docker compose to simulate base environment.

Please visit folder [scripts](/scripts)

Information about docker compose [here](https://docs.docker.com/compose/install/) and command line reference [here](https://docs.docker.com/compose/reference/overview/). 

Enjoy tour journey in Spring Cloud Dataflow Framework. 


## Docker images repository

Here the docker images sources repository:

* [spring-dataflow-docker](https://github.com/hellgate75/spring-dataflow-docker/tree/flow-centric)




## Coming soon

Upcoming branch with a compose whicb main news are Spring Cloud Dataflow Server and the Spring Cloud Skipper Server you can scale as you can with you system resources and you will be able to scale individually for the 3 microservices, registered into the Server via catalogue (source, process and sink). 
We will provide as well the 3 analytics microservices with some automation on the definition and recognition of model types, indexes, and some new spatial concepts. Autoplacing indexes required by the analytics nodes, via model databdatabase (missing in this release).
The use of another streaming engine will realize the data push in the metadata sourcing microservice channels and it will be used to pushback responses from the analytics sink moctoservice after the analytics group computation.
So let's get ready for a more intensive experience on the dataflow universe ...



## License

The library is licensed with [CC0 v. 1.0](/LICENSE) clauses, with prior authorization of author before any production or commercial use. Use of this library or any extension is prohibited due to high risk of damages due to improper use. No warranty is provided for improper or unauthorized use of this library or any implementation.

Any request can be prompted to the author [Fabrizio Torelli](https://www.linkedin.com/in/fabriziotorelli) at the follwoing email address:

[hellgate75@gmail.com](mailto:hellgate75@gmail.com)
