package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by Usuario on 24/11/2016.
 */


public class ToGetCoord {
    @SerializedName("phone")
    private String phone;
    @SerializedName("key")
    private String key;
    @SerializedName("matches")
    private ArrayList<String> matches;

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

    public ArrayList<String> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<String> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return "{" +
                "phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                ", matches=" + matches +
                '}';
    }
}
