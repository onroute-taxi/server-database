package com.onroute.database;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.spi.KernelContext;
import org.neo4j.kernel.lifecycle.Lifecycle;

/**
 * @author steven.enamakel@gmail.com
 */
public class OnrouteKernelExtensionFactory extends KernelExtensionFactory<OnrouteKernelExtensionFactory.Dependencies> {
    protected OnrouteKernelExtensionFactory(String key) {
        super("OnrouteExtension");
        System.out.printf("initinit");
    }


    public interface Dependencies {
        GraphDatabaseService getGraphDatabaseService();
        Config getConfig();
    }


    @Override public Lifecycle newInstance(KernelContext context, Dependencies dependencies) throws Throwable {
        System.out.println("fuck you");
        return new OnrouteKernelExtension();
    }
}
