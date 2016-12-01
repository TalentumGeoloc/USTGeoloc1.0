package com.example.olaznog59.proyectoustvista.modelo.persistencia;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.RespuestaServidor;
import com.example.olaznog59.proyectoustvista.modelo.entidad.Contacto;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Usuario;


import java.util.ArrayList;

/**
 * Created by juanmgar on 22/11/16.
 */

/*Esta es la clase que guarda los contactos, los crea y los borra*/
public class DaoContactos {

    private DataBaseHelper db = null;
    private SQLiteDatabase sql = null;

    public DaoContactos(Context context){
        db = new DataBaseHelper(context);
        try {
            sql = db.getWritableDatabase();
        }catch (Exception e){
            Log.i("DaoContactos","Error abriendo database: " + e.getMessage());
        }
    }

    public void cerrar(){
        db.close();
    }

    public void crearContacto(Usuario usuario){

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", usuario.getNombre());
        contentValues.put("telefono", usuario.getPhone());
        contentValues.put("latitud", usuario.getLat());
        contentValues.put("longitud", usuario.getLon());

        sql.insert(DataBaseHelper.TABLA,null,contentValues);
    }


    public void nuevoContacto(Contacto contacto){

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre",contacto.getNombre());
        contentValues.put("telefono",contacto.getTelefono());
        contentValues.put("latitud",contacto.getLatitud());
        contentValues.put("longitud",contacto.getLongitud());

        sql.insert(DataBaseHelper.TABLA,null,contentValues);
    }
    public void insertarContactosCompletos(RespuestaServidor arrayContactos) {

        ArrayList<Usuario> arrayList = arrayContactos.getCoordinates();

        for (Usuario usuario : arrayList) {

            crearContacto(usuario);
        }
    }



    public ArrayList<Usuario> obtenerContactos(){
        String [] columnasABuscar = {"nombre","telefono","latitud", "longitud"};
        Cursor c = sql.query(DataBaseHelper.TABLA,columnasABuscar,null,null,null,null,null,null);
        ArrayList<Usuario> listaUsuarios = null;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            listaUsuarios = new ArrayList<Usuario>();
            do {
                String nombre = c.getString(0);
                String telefono = c.getString(1);
                double latitud = c.getDouble(2);
                double longitud = c.getDouble(3);

                Usuario usuario = new Usuario();
                usuario.setNombre(nombre);
                usuario.setPhone(telefono);
                usuario.setLat(latitud);
                usuario.setLon(longitud);

                listaUsuarios.add(usuario);
            }while (c.moveToNext());//Movemos el cursor al siguiente registro devuelto
        }
        db.close();
        Log.d("DaoContactos", "obtenerContactos -> Usuarios recuperado");

        return listaUsuarios;
    }

    public boolean borrarContacto(int id){
        int i = sql.delete(DataBaseHelper.TABLA,"_id="+id,null);
        return i >0;
    }
}
