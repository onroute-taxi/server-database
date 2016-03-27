package com.backseatmedia.database.engines;

import com.backseatmedia.database.App;
import com.graphaware.reco.generic.config.SimpleConfig;
import lombok.Getter;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;


public class AppRecoConfig extends SimpleConfig {
    @Inject @Getter GraphDatabaseService graphDb;
    @Inject @Getter SpatialDatabaseService spatialDb;


    public AppRecoConfig(int limit) {
        super(limit);
        App.inject(this);
    }


    public AppRecoConfig(int limit, long maxTime) {
        super(limit, maxTime);
        App.inject(this);
    }
}