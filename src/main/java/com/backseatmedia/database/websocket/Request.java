package com.backseatmedia.database.websocket;

import com.backseatmedia.database.models.SessionModel;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


/**
 * This class represents a request. This is what we'll expect to be sent from the websocket client.
 */
@Data
public class Request {
    @Expose String resource = "";
    @Expose String function = "";
    @Expose String data;
    @Expose SessionModel session;
}