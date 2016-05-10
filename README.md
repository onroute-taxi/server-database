Onroute Database
================

The Onroute database plugin is a plugin for the [Neo4j](http://neo4j.com) server that allows Neo4j 

 - to track the location and activity of Onroute tablets
 - Manage/Serve advertismentsand & content and serve it to the tablets with the help of recommendation engines.
 - Ensure latency is to a minimum by using websockets for communication.

The plugin uses the Neo4j [spatial plugin](https://github.com/neo4j-contrib/spatial) (for running geospatial queries) and uses Graphware's [recommendation engine library](https://github.com/graphaware/neo4j-reco) (for generating content and advertisement recommendations).

This repository contains the code to implement Onroute's database as a [Neo4j plugin](http://neo4j.com/docs/stable/server-plugins.html). 


Requirements
------------
 - Maven 
 - An IDE, IntelliJ or Eclipse
 - Neo4j 


Installation
------------
 - [Download the Neo4j binary](http://neo4j.com/download) and save it as ```./neo4j```. You should be able to run the server by running the command ```./neo4j/bin/neo4j console```. 
 - Compile the database plugin by running ```mvn clean package```. This creates a ```target/Onroute.jar``` JAR file.
 - Copy/Link the JAR file onto the neo4j's plugins folder. ```cp ./target/Onroute.jar ./neo4j/plugins/Onroute.jar```.
 - Run Neo4j. ```./neo4j/bin/neo4j start```.

 The server would be running at [http://localhost:7474](http://localhost:7474)

 A quick [setup file](./setup.sh) is given to you for reference.


How it works
------------
The generated JAR plugin contains the neo4j spatial plugin, recommendation engines and the websocket server. When Neo4j starts, the plugin initializes a websocket server and starts listening for connections from any Onroute tablets.

The websocket server by default listens to port 1414. To change it, go to `conf/neo4j.properties` and add

```
## hostname and port the websocket server shall listen to
## defaults to 0.0.0.0:1414 if not set
websocket_host=0.0.0.0:9090
```

Every websocket message from the tablet will ask for a ```resource``` and a ```function```. 

A ```resource``` can be anything from Advertisements, Movies, Tablets, Passenger etc.. 

A ```function``` is a specific action that is done with the resource it comes with (checking in, viewing advertisement, etc).

With the resource and function sepcified, the database can understand what to execute and return the results back to the Onroute tablet. See the different resources for a better understanding.
