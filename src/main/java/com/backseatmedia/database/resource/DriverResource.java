package com.backseatmedia.database.resource;

import com.backseatmedia.database.models.SessionModel;
import com.backseatmedia.database.resource.base.BaseResource;
import com.backseatmedia.database.websocket.Response;
import org.neo4j.graphdb.NotFoundException;


/**
 * This endpoint is used
 */
public class DriverResource extends BaseResource {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "driver";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Response handleRequest(String function, SessionModel session, String data) throws Exception {
        switch (function) {
            case "login":
                break;
            case "register":
                break;
            case "get_info":
                break;
            default:
                throw new NotFoundException("no function found");
        }
        return Response.returnSuccess(session);
    }
}
