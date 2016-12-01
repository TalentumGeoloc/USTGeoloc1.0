package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response;



import com.google.gson.annotations.SerializedName;

/**
 * Created by Usuario on 23/11/2016.
 */

public class Register {

    @SerializedName("key")
    private String key;
    @SerializedName("error_description")
    private String errorDescription;
    @SerializedName("error_code")
    private int errorCode;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        return "Register{" +
                "key='" + key + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
