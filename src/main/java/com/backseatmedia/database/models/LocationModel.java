package com.backseatmedia.database.models;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;


public class LocationModel {
    @Expose @Getter @Setter public double latitude;
    @Expose @Getter @Setter public double longitude;
    @Expose @Getter @Setter public double radius;


    public LocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public LocationModel(double latitude, double longitude, double radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
}