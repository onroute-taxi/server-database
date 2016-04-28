package com.onroute.database;


import dagger.ObjectGraph;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;


/**
 * This class is where the Dagger depedency graph is initialized.
 */
public class App {
    static ObjectGraph applicationGraph;


    public static void init(GraphDatabaseService graphDatabaseService, SpatialDatabaseService spatialDatabaseService) {
        applicationGraph = ObjectGraph.create().plus(
                new DatabaseModule(graphDatabaseService, spatialDatabaseService));
    }


    public static <T> T inject(T instance) {
        if (applicationGraph == null) return null;
        return applicationGraph.inject(instance);
    }


    public static <T> T get(Class<T> instance) {
        if (applicationGraph == null) return null;
        return applicationGraph.get(instance);
    }
}