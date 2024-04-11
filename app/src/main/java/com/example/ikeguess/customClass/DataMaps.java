package com.example.ikeguess.customClass;

public class DataMaps {

    public final String name;
    public final String url;
    public final Double latitude;
    public final Double longitude;
    public final String type;
    public final Integer radius;


    public DataMaps(String name, String url, Double latitude, Double longitude, String type, Integer radius) {
        this.name = name;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.radius = radius;
    }
}