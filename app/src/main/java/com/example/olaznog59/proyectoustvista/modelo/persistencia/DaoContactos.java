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
    public static final String TAG = "DaoContactos";

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

        boolean existe = buscarUsuario(usuario);
        if (!existe){
            //si no existe se crea el nuevo usuario en la base de datos
            ContentValues contentValues = new ContentValues();
            contentValues.put("nombre", usuario.getNombre());
            contentValues.put("telefono", usuario.getPhone());
            contentValues.put("latitud", usuario.getLat());
            contentValues.put("longitud", usuario.getLon());

            sql.insert(DataBaseHelper.TABLA,null,contentValues);
        } else {
            //si exite, se actualizan los valores de latitud y longitud
            ContentValues contentValues = new ContentValues();
            contentValues.put("latitud",usuario.getLat());
            contentValues.put("longitud",usuario.getLon());
            sql.update(DataBaseHelper.TABLA,//tabla a modificar
                    contentValues,//campos a modificar
                    "telefono="+usuario.getPhone(),//dónde modificar
                    null);
        }



    }



    public boolean buscarUsuario (Usuario usuario){
        boolean existe = false;
        //Primero comprobamos si el usuario existe en bbdd
        String [] columnasABuscar = {"nombre","telefono"};

        Log.d(TAG, "Vamos a si el usuario existe en la base de datos");
        Cursor c = sql.query(
                DataBaseHelper.TABLA, //tabla donde se va a buscar
                columnasABuscar, //columnas a buscar
                "telefono=" + usuario.getPhone(), //where
                null,null,null,null,null);

        if (c.moveToNext()){
            Log.d(TAG, "El contacto existe, nombre: "+ c.getString(0) + " tlf: " + c.getString(1));
            existe = true;
        }
        return existe;
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
