package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Usuario on 25/11/2016.
 */

public class Usuario {
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("phone")
    private String phone;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", phone='" + phone + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
