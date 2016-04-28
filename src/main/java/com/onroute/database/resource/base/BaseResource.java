package com.onroute.database.resource.base;

import com.onroute.database.App;
import com.google.gson.Gson;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;


/**
 * This is the base class from all Resource handlers.
 */
public abstract class BaseResource implements ResourceHandler {
    @Inject protected GraphDatabaseService graphDb;
    @Inject protected SpatialDatabaseService spatialDb;
    @Inject protected Gson gson;


    public BaseResource() {
        App.getApplicationGraph().inject(this);
    }
}