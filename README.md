Onroute Database
================

This file consists of the database for the onroute.


Installation
------------

Configuration
-------------
The websocket server by default listens to port 1414. To change it, go to `conf/neo4j.properties` and add

```
## hostname and port the websocket server shall listen to
## defaults to 0.0.0.0:1414 if not set
websocket_host=0.0.0.0:9090
```