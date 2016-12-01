package com.example.olaznog59.proyectoustvista.modelo.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juanmgar on 21/11/16.
 */

//Esta clase nos ayuda con la gestion del ciclo de vida de la BD


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "agenda";
    private static final int VERSION = 1;

    public static final String TABLA = "contactos";


    //creamos la base de datos

    private static final String DB_CREATE_AGENDA = "create table "+ TABLA +
            " (_id integer primary key autoincrement," +
            "nombre text not null," +
            "telefono text not null," +
            "latitud float not null,"+
            "longitud float not null)";

//Este metodo se va a ejecutar la primera vez que creemos un objeto
    //de la clase DataBaseHelper

    public DataBaseHelper(Context context){
        super(context,NOMBRE_BD,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_AGENDA);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + TABLA);
        db.execSQL(DB_CREATE_AGENDA);
    }
}
