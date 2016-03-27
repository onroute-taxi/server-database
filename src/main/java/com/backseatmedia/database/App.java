package com.backseatmedia.database;


import com.backseatmedia.database.websocket.WebsocketServer;
import dagger.ObjectGraph;
import lombok.Getter;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.inject.Inject;


/**
 * This class is entry point for the server. Here we setup two things. The Neo4J community server and the websocket
 * server.
 * <p/>
 * The real magic, of course happens at the WebsocketServer. This class is used to simply initialize stuff.
 */
public class App {
    // Boolean to start the community server on port 7474 or not.
    public static Boolean DEBUG = true;

    @Inject GraphDatabaseService graphDb;
    @Inject WebsocketServer websocketServer;

    @Getter static ObjectGraph applicationGraph;


    public static <T> T inject(T instance) {
        if (applicationGraph == null) return null;
        return applicationGraph.inject(instance);
    }


    public static <T> T get(Class<T> instance) {
        if (applicationGraph == null) return null;
        return applicationGraph.get(instance);
    }


    public static void main(String[] args) {
        // First, create the dependency graph
        applicationGraph = ObjectGraph.create()
                .plus(new DatabaseModule());

        // Create an instance of the app (from the dep. graph)
        final App app = applicationGraph.get(App.class);

        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                app.graphDb.shutdown();
                System.out.println("Shutting down properly");
            }
        });

        // Start the websocket server!
        try {
            app.websocketServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}