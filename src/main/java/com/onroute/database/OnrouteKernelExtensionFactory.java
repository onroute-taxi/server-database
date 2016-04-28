package com.onroute.database;

import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }


    @Override public Lifecycle newInstance(KernelContext context, Dependencies dependencies) {
        logger.info("initializing");
        GraphDatabaseService graphDatabaseService = dependencies.getGraphDatabaseService();
        SpatialDatabaseService spatialService = new SpatialDatabaseService(graphDatabaseService);
        return new OnrouteKernelExtension(graphDatabaseService, spatialService);
    }
}
