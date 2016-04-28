#!/bin/sh

# Download and setup Neo4j
wget http://neo4j.com/artifact.php?name=neo4j-community-2.3.2-unix.tar.gz -O neo4j-community-2.3.2-unix.tar.gz
tar xvf neo4j-community-2.3.2-unix.tar.gz
mv ./neo4j-community-2.3.2 ./neo4j

# Compile the package
mvn clean package

# Link it to the server's plugin folder
ln -sf `pwd`/target/Onroute.jar ./neo4j/plugins/onroute.jar

echo "done! now run: ./neo4j/bin/neo4j start"