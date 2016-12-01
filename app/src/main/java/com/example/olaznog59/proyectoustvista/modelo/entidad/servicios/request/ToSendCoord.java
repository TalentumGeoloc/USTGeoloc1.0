package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Usuario on 24/11/2016.
 */



public class ToSendCoord {

    @SerializedName("phone")
    private String phone;
    @SerializedName("key")
    private String key;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "{" +
                "phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
