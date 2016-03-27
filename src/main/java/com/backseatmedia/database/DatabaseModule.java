package com.backseatmedia.database;


import com.backseatmedia.database.resource.*;
import com.backseatmedia.database.resource.base.BaseResource;
import com.backseatmedia.database.resource.base.ResourceHandler;
import com.backseatmedia.database.websocket.WebsocketServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.server.CommunityBootstrapper;

import javax.inject.Singleton;
import java.io.File;
import java.net.InetSocketAddress;
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
    // Variables for the graph database.
    final File STOREDIR = new File("data/graph.graphDb");
    final File PATHTOCONFIG = new File("config/neo4j.properties");
    final Boolean ENABLE_COMMUNITY_SERVER = true;

    // Settings for the websocket server
    static String WEBSOCKET_IP = "0.0.0.0";
    static int WEBSOCKET_PORT = 1414;

    // Instance of Neo4j database
    CommunityBootstrapper bootstraper;


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
        GraphDatabaseService graphDb;

        // TODO: Get rid of the community bootstraper and boot the community server directly!
        if (ENABLE_COMMUNITY_SERVER) {
            // This hack tries to start the community server and also tries to retrieve the GraphDB service from it.
            bootstraper = new CommunityBootstrapper();
            CommunityBootstrapper.start(bootstraper, new String[]{});
            graphDb = bootstraper.getServer().getDatabase().getGraph();
        } else {
            // Initialize a GraphDB instance
            graphDb = new GraphDatabaseFactory()
                    .newEmbeddedDatabaseBuilder(STOREDIR)
                    .loadPropertiesFromFile(PATHTOCONFIG.getAbsolutePath())
                    .newGraphDatabase();
        }

        return graphDb;
    }


    @Provides
    @Singleton
    SpatialDatabaseService providesSpatialDB(GraphDatabaseService graphDb) {
        return new SpatialDatabaseService(graphDb);
    }


    @Provides
    @Singleton
    WebsocketServer providesWebsocketServer() {
        try {
            InetSocketAddress websocket_address = new InetSocketAddress(WEBSOCKET_IP, WEBSOCKET_PORT);
            return new WebsocketServer(websocket_address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
