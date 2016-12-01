package com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by Usuario on 23/11/2016.
 */



public class Matches {

    @SerializedName("matches")
    private ArrayList<String> matches;
    @SerializedName("error_description")
    private String error_description;
    @SerializedName("error_code")

    private int error_code;

    public ArrayList<String> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<String> matches) {
        this.matches = matches;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }


    @Override
    public String toString() {
        return "Matches{" +
                "matches=" + matches +
                ", error_description='" + error_description + '\'' +
                ", error_code=" + error_code +
                '}';
    }

}
