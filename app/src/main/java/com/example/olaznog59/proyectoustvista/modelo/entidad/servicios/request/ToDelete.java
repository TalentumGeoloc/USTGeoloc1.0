package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juanm on 01/12/2016.
 */

public class ToDelete {
    @SerializedName("phone")
    private String phone;
    @SerializedName("key")
    private String key;

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

    @Override
    public String toString() {
        return "ToDelete{" +
                "phone='" + phone + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
