package com.example.olaznog59.proyectoustvista.modelo.negocio.singletons;

import android.util.Log;

import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Usuario;

import java.util.ArrayList;

/**
 * Created by Usuario on 28/11/2016.
 */

public class Clave {
    private static Clave instance = null;
    private String phone;
    private String  key;
    private ArrayList<String> matches;
    private ArrayList<Usuario> arrayUsuarios;

    private Clave() {
        Log.d("Clave","Creado objeto Clave");
    }



    public void inicialice(String phone, String key){
        this.phone = phone;
        this.key = key;

    }

    public static Clave getInstance(){
        if (instance == null){
            instance = new Clave();
        }
        return instance;
    }

    public String getPhone() {
        return phone;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<String> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<String> matches) {
        this.matches = matches;
    }

    public ArrayList<Usuario> getArrayUsuarios() {
        return arrayUsuarios;
    }

    public void setArrayUsuarios(ArrayList<Usuario> arrayUsuarios) {
        this.arrayUsuarios = arrayUsuarios;
    }

}
