package com.onroute.database.resource.base;

import com.onroute.database.models.SessionModel;
import com.onroute.database.websocket.Response;

public interface ResourceHandler {
    /**
     * Get a unique name for the resource handler. This value will be used by the Android tablet to
     * specify which resource it wants.
     *
     * @return A {@link String} that represents the short name of this resource handler.
     */
    String getName();


    /**
     * This function handles the requests from a websocket connection. Each resource will implement
     * this methods in their own way.
     *
     * @param function The function that was asked to be called.
     * @param session  The session object that is passed.
     * @param data     Addition JSON data that is passed for the function.
     * @return A {@link Response} object that represents the sesssion if there are no errors.
     * @throws Exception Any exception that gets thrown will be forwarded to the caller function.
     */
    Response handleRequest(String function, SessionModel session, String data)
            throws Exception;
}
