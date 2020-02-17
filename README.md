<p align="center"><image width="250" height="250" src="images/flow-centric-poc-logo.png">&nbsp;<image width="120" height="80" src="images/dataflow-logo.png"></image></p><br/>
<br/>
<p align="right"><img src="https://travis-ci.org/hellgate75/flow-centric-poc.svg?branch=master" alt="trevis-ci" width="98" height="20" />&nbsp;<a href="https://travis-ci.org/hellgate75/flow-centric-poc">Check last build on Travis-CI</a></p><br/>
<br/>

# Flow Centric PoC


Unknown Structure Stream flows PoC project. Goal is to give Proof of Concept for Stream Data Meaning and Recognizing base on complex JSON data model, based on Spring DataFlow technology.

## What we want fix?

You have small but complex data, and you want easily get ready for any analytics tools with less burn down
and expenses possible. So we tried at the end the use of BSON library and the Spring Cloud Dataflow framework, for running a 'continuous streaming'...



## First level PoC development.

Here a sample architecture.



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




## License

The library is licensed with [CC0 v. 1.0](/LICENSE) clauses, with prior authorization of author before any production or commercial use. Use of this library or any extension is prohibited due to high risk of damages due to improper use. No warranty is provided for improper or unauthorized use of this library or any implementation.

Any request can be prompted to the author [Fabrizio Torelli](https://www.linkedin.com/in/fabriziotorelli) at the follwoing email address:

[hellgate75@gmail.com](mailto:hellgate75@gmail.com)
