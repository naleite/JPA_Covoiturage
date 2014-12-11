#!/bin/sh
mvn dependency:copy-dependencies
mkdir data
cd data
java -cp ../target/dependency/hsqldb-2.2.8.jar org.hsqldb.Server
