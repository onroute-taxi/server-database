package com.onroute.database;


import dagger.ObjectGraph;
import lombok.Setter;


/**
 * This class is entry point for the server. Here we setup two things. The Neo4J community server and the websocket
 * server.
 * <p/>
 * The real magic, of course happens at the WebsocketServer. This class is used to simply initialize stuff.
 */
public class App {
    @Setter static ObjectGraph applicationGraph;


    public static <T> T inject(T instance) {
        if (applicationGraph == null) return null;
        return applicationGraph.inject(instance);
    }


    public static <T> T get(Class<T> instance) {
        if (applicationGraph == null) return null;
        return applicationGraph.get(instance);
    }
}