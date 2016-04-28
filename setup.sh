#!/bin/sh

# Download and setup Neo4j
wget http://neo4j.com/artifact.php?name=neo4j-community-2.3.2-unix.tar.gz -O neo4j-community-2.3.2-unix.tar.gz
tar xvf neo4j-community-2.3.2-unix.tar.gz
mv ./neo4j-community-2.3.2-unix ./neo4j