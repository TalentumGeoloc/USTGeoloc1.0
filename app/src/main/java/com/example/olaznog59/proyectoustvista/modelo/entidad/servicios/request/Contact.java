package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by Usuario on 24/11/2016.
 */



public class Contact {

    @SerializedName("phone")
    private String phone;
    @SerializedName("key")
    private String key;
    @SerializedName("contacts")
    private ArrayList<String> contacts;

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

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "{" +
                "phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
