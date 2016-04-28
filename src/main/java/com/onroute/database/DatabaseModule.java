package com.onroute.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onroute.database.resource.*;
import com.onroute.database.resource.base.BaseResource;
import com.onroute.database.resource.base.ResourceHandler;
import com.onroute.database.websocket.WebsocketServer;
import dagger.Module;
import dagger.Provides;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(
        injects = {
                App.class,

                AdvertisementResource.class,
                DriverResource.class,
                JourneyResource.class,
                MediaResource.class,
                PassengerResource.class,
                TabletResource.class,
                BaseResource.class,

                WebsocketServer.class
        },
        library = true
)
public class DatabaseModule {
    final GraphDatabaseService graphDatabaseService;
    final SpatialDatabaseService spatialDatabaseService;


    public DatabaseModule(GraphDatabaseService graphDatabaseService,
                          SpatialDatabaseService spatialDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
        this.spatialDatabaseService = spatialDatabaseService;
    }


    @Provides
    @Singleton
    Gson providesGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }


    @Provides
    @Singleton
    GraphDatabaseService providesGraphDB() {
        return graphDatabaseService;
    }


    @Provides
    @Singleton
    SpatialDatabaseService providesSpatialDB() {
        return spatialDatabaseService;
    }


    @Provides
    @Singleton
    AdvertisementResource providesAdvertisementResource() {
        return new AdvertisementResource();
    }


    @Provides
    @Singleton
    MediaResource providesMediaResource() {
        return new MediaResource();
    }


    @Provides
    @Singleton
    DriverResource providesDriverResource() {
        return new DriverResource();
    }


    @Provides
    @Singleton
    JourneyResource providesJourneyResource() {
        return new JourneyResource();
    }


    @Provides
    @Singleton
    PassengerResource providesPassengerResource() {
        return new PassengerResource();
    }


    @Provides
    @Singleton
    TabletResource providesTabletResource() {
        return new TabletResource();
    }


    @Provides
    @Singleton
    Map<String, ResourceHandler> providesResourceHandlers() {
        List<ResourceHandler> resourceHandlers = Arrays.<ResourceHandler>asList(
                App.get(MediaResource.class),
                App.get(DriverResource.class),
                App.get(JourneyResource.class),
                App.get(TabletResource.class),
                App.get(PassengerResource.class),
                App.get(AdvertisementResource.class)
        );

        Map<String, ResourceHandler> results = new HashMap<>();
        for (ResourceHandler resource : resourceHandlers) results.put(resource.getName(), resource);

        return results;
    }
}
