package com.backseatmedia.database.websocket;


import com.backseatmedia.database.App;
import com.backseatmedia.database.models.SessionModel;
import com.backseatmedia.database.resource.base.ResourceHandler;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.neo4j.graphdb.NotFoundException;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.MissingFormatArgumentException;


/**
 * This class represents the Websocket server which is mainly responsible for communicating with the tablet. This class
 * handles the Requests, calls the appropriate resource and delivers the response properly.
 * <p/>
 * The response and request is serialized to JSON.
 * <p/>
 * TODO: Have serialization to happen directly.
 */
public class WebsocketServer extends WebSocketServer {
    @Inject Gson gson;
    @Inject Map<String, ResourceHandler> resourceHandlers;


    /**
     * Constructor to initialize the websocket properly
     *
     * @param address The address to run the websocket server
     * @throws UnknownHostException
     */
    public WebsocketServer(InetSocketAddress address)
            throws UnknownHostException {
        super(address);
        App.inject(this);

        System.out.println(String.format("websocket initialized on %s:%d", address.getHostString(),
                address.getPort()));
    }


    void debug(String message) {
        if (App.DEBUG) System.out.println(message);
    }


    /**
     * This function is called whenever a socket connection is opened.
     */
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        debug("socket opened");
    }


    /**
     * This function is called whenever a socket connection is closed.
     */
    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        debug("socket closed");
    }


    /**
     * This function is called whenever a message from the websocket client is recieved.
     */
    @Override
    public void onMessage(WebSocket webSocket, String message) {
        debug("got message: " + message);
        Request request = gson.fromJson(message, Request.class);

        // Erase all the commands.
        request.getSession().setCommands(new ArrayList<SessionModel.Command>());

        // Send it the appropriate resource handler and get the response
        Response responseModel = processRequest(request);
        responseModel.setRequest(request);

        // Return the output json to the websocketServer
        String output = gson.toJson(responseModel);
        System.out.println("sent: " + output);
        try {
            webSocket.send(output);
        } catch (MissingFormatArgumentException e) {
            // For some reason we keep getting this exception. So we catch it and ignore it here.
        }
    }


    /**
     * This function is called whenever there is an error that has occured with the websocket.
     *
     * @param webSocket The socket that made the error.
     * @param exception The Exception that occured.
     */
    @Override
    public void onError(WebSocket webSocket, Exception exception) {
        debug("error: " + exception.getMessage());
        exception.printStackTrace();
    }


    /**
     * This function is mainly responsible for parsing the request, processing it and returning the output to the
     * websocketServer.
     *
     * @param request The response that was sent to the websocketServer
     * @return Response The response that should be sent back.
     */
    private Response processRequest(Request request) {
        try {
            ResourceHandler handler = resourceHandlers.get(request.getResource());
            if (handler != null)
                return handler.handleRequest(request.getFunction(),
                        request.getSession(), request.getData());
        } catch (Exception e) {
            // If there was any exception then we return that in the form of the request.
            e.printStackTrace();
            return Response.returnException(e);
        }

        return Response.returnException(new NotFoundException("no resource specified"));
    }
}
