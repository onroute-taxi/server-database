package com.onroute.database;

import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.config.Setting;
import org.neo4j.helpers.HostnamePort;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.neo4j.helpers.Settings.HOSTNAME_PORT;
import static org.neo4j.helpers.Settings.setting;


/**
 * @author steven.enamakel@gmail.com
 */
public class OnrouteKernelExtensionFactory extends KernelExtensionFactory<OnrouteKernelExtensionFactory.Dependencies> {
    private static final Logger logger = LoggerFactory.getLogger(OnrouteKernelExtensionFactory.class);


    public OnrouteKernelExtensionFactory() {
        super("onroute");
    }


    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();
        Config getConfig();
    }


    @Override public Lifecycle newInstance(KernelContext context, Dependencies dependencies) {
        logger.info("initializing");
        Config config = dependencies.getConfig();

        GraphDatabaseService graphDatabaseService = dependencies.getGraphDatabaseService();
        SpatialDatabaseService spatialService = new SpatialDatabaseService(graphDatabaseService);

        Setting<HostnamePort> hostnamePort = setting("websocket_host", HOSTNAME_PORT, "0.0.0.0:1414");

        return new OnrouteKernelExtension(graphDatabaseService, spatialService, config.get(hostnamePort));
    }
}
