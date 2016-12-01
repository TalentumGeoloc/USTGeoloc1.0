package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by Usuario on 23/11/2016.
 */


public class RespuestaServidor {

    @SerializedName("matches")
    private ArrayList<Usuario> usuarios;
    @SerializedName("error_description")
    private String errorDescription;
    @SerializedName("error_code")
    private int errorCode;

    public ArrayList<Usuario> getCoordinates() {
        return usuarios;
    }

    public void setCoordinates(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "RespuestaServidor{" +
                "coordinates=" + usuarios +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorCode=" + errorCode +
                '}';

    }
}
