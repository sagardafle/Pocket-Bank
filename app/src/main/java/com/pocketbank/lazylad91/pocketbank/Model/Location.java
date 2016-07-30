package com.pocketbank.lazylad91.pocketbank.Model;

import java.io.Serializable;

/**
 * Created by Parteek on 7/30/2016.
 */
public class Location implements Serializable {
    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", name='" + name + '\'' +
                '}';
    }

    public Location() {

    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Double lat;
    private Double lng;
    private String name;
}
