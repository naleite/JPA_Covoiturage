mvn dependency:copy-dependencies
cd data
java -cp ../target/dependency/hsqldb-2.2.8.jar org.hsqldb.Server
