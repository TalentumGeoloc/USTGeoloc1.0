package com.example.olaznog59.proyectoustvista.modelo.contentprovider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Usuario;

import java.util.ArrayList;

/**
 * Created by Usuario on 27/11/2016.
 */

//PARTES JUNTADAS EN UN ÚNICO PROYECTO
public class ContactosAgenda {

    private Context context;

    public ContactosAgenda(Context context) {
        this.context = context;
    }

    public Usuario obtenerNombres(Usuario usuario){

        String telefono = usuario.getPhone();

        String[] projeccion = new String[] { ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        ContentResolver resolver=context.getContentResolver();
        Cursor c = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + telefono + "%'",
                null,
                null);

        String st2 = "";
        while (c.moveToNext()){
            String str1 = c.getString(0);
            st2 = c.getString(1);
            String str3 = c.getString(2);
            Log.d("---------------","obtenido" + c.getString(0) + " " + c.getString(1) + " " + c.getString(2));

        }
        usuario.setNombre(st2);
        return usuario;
    }



    public ArrayList<String> obtenerNumeros(){

        ContentResolver resolver=context.getContentResolver();

        String[] projeccion = new String[] { ContactsContract.Data._ID, //0
                ContactsContract.Data.DISPLAY_NAME, //1
                ContactsContract.CommonDataKinds.Phone.NUMBER}; //2
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                sortOrder);

        ArrayList<String > contactos = new ArrayList<>();

        while (c.moveToNext()){
            Log.d("ContactosAgenda", c.getString(1) + " " + c.getString(2));
            String numero = c.getString(2);
            String numeroSinParentisis = numero.replaceAll("\\(", "").replaceAll("\\)","");
            String numeroSinGuiones = numeroSinParentisis.replaceAll("\\-", "");
            String numeroSinEspacios = numeroSinGuiones.replaceAll(" ", "");
            Log.d("ContactosAgenda","cargarNumeros -> Numero parseado " + numeroSinEspacios );

            contactos.add(numeroSinEspacios);

        }

        return contactos;
    }

}
