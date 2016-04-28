package com.onroute.database;

import com.onroute.database.websocket.WebsocketServer;
import dagger.ObjectGraph;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.HostnamePort;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author steven.enamakel@gmail.com
 */
public class OnrouteKernelExtension implements Lifecycle {
    private static final Logger logger = LoggerFactory.getLogger(OnrouteKernelExtension.class);

    WebsocketServer websocketServer;
    ObjectGraph applicationGraph;
    HostnamePort hostnamePort;


    public OnrouteKernelExtension(GraphDatabaseService graphDatabaseService,
                                  SpatialDatabaseService spatialDatabaseService, HostnamePort hostnamePort) {
        this.hostnamePort = hostnamePort;

        // First, create the dependency graph
        applicationGraph = ObjectGraph.create().plus(
                new DatabaseModule(graphDatabaseService, spatialDatabaseService));
        App.setApplicationGraph(applicationGraph);
    }


    @Override public void init() throws Throwable {

    }


    @Override public void start() throws Throwable {
        // Initialize the websocker server
        InetSocketAddress websocket_address = new InetSocketAddress(hostnamePort.getHost(), hostnamePort.getPort());
        websocketServer = new WebsocketServer(websocket_address);

        logger.debug("starting websocket server");
        websocketServer.start();
    }


    @Override public void stop() throws Throwable {
        logger.debug("stopping websocket server");
        websocketServer.stop();
    }


    @Override public void shutdown() throws Throwable {

    }
}
