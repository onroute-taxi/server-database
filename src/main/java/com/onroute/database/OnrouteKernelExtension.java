package com.onroute.database;

import com.onroute.database.websocket.WebsocketServer;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.HostnamePort;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * This is the entry point of the application. The Neo4J server launches this class when the kernel loads. This
 * lifecycle class is mainly used for starting/stopping the {@link WebsocketServer}.
 *
 * The {@link WebsocketServer} is where the main action happens.
 *
 * @author steven.enamakel@gmail.com
 */
public class OnrouteKernelExtension implements Lifecycle {
    private static final Logger logger = LoggerFactory.getLogger(OnrouteKernelExtension.class);

    private WebsocketServer websocketServer;
    private HostnamePort hostnamePort;
    private GraphDatabaseService graphDatabaseService;
    private SpatialDatabaseService spatialDatabaseService;


    public OnrouteKernelExtension(GraphDatabaseService graphDatabaseService,
                                  SpatialDatabaseService spatialDatabaseService, HostnamePort hostnamePort) {
        this.hostnamePort = hostnamePort;
        this.graphDatabaseService = graphDatabaseService;
        this.spatialDatabaseService = spatialDatabaseService;
    }


    @Override public void init() throws Throwable {
        App.init(graphDatabaseService, spatialDatabaseService);
    }


    @Override public void start() throws Throwable {
        logger.info("initializing the websocket server at " + hostnamePort.toString());
        InetSocketAddress websocketAddress = new InetSocketAddress(hostnamePort.getHost(), hostnamePort.getPort());
        websocketServer = new WebsocketServer(websocketAddress);

        logger.info("starting the websocket server");
        websocketServer.start();
    }


    @Override public void stop() throws Throwable {
        logger.info("stopping the websocket server");
        websocketServer.stop();
    }


    @Override public void shutdown() throws Throwable {

    }
}
