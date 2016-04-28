package com.onroute.database.websocket;

import com.onroute.database.models.SessionModel;
import com.google.gson.annotations.Expose;
import lombok.Data;

/**
 * This class represents a Response. This is what is sent back to the Websocket client.
 */
@Data
public class Response {
    @Expose String status = "ok";
    @Expose String error_message;
    @Expose String error;
    @Expose SessionModel session;
    @Expose Request request;


    Response(SessionModel session) {
        this.session = session;
    }


    /**
     * Factory function that generates a response if an Exception has occured.
     *
     * @param e The Exception that has occured.
     * @return The genreated Response
     */
    public static Response returnException(Exception e) {
        Response responseModel = new Response(null);

        responseModel.status = "error";
        responseModel.error = e.getClass().getSimpleName();
        responseModel.error_message = e.getMessage();
        return responseModel;
    }


    /**
     * Factory function to create a 'success' response
     *
     * @param session The session to be returned.
     * @return The genreated Response
     */
    public static Response returnSuccess(SessionModel session) {
        return new Response(session);
    }
}