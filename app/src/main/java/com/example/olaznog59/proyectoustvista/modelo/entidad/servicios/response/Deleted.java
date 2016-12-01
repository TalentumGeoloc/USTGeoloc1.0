package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by juanm on 01/12/2016.
 */

public class Deleted {
    @SerializedName("error_description")
    private String errorDescription;
    @SerializedName("error_code")
    private int errorCode;

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
        return "Deleted{" +
                "errorDescription='" + errorDescription + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
