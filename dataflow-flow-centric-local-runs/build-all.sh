#!/bin/sh
PWD="$(pwd)"
cd ..
mvn clean install
cd $PWD
