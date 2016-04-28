package com.onroute.database;

import org.neo4j.kernel.lifecycle.Lifecycle;

/**
 * @author steven.enamakel@gmail.com
 */
public class OnrouteKernelExtension implements Lifecycle{
    @Override public void init() throws Throwable {
        System.out.println("fucking init");
    }


    @Override public void start() throws Throwable {

    }


    @Override public void stop() throws Throwable {

    }


    @Override public void shutdown() throws Throwable {

    }
}
