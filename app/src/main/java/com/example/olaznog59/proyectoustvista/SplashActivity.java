package com.example.olaznog59.proyectoustvista;

import android.*;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.olaznog59.proyectoustvista.modelo.broadcasts.AlertReceiver;
import com.example.olaznog59.proyectoustvista.modelo.negocio.PermissionUtils;
import com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest.GestorServer;
import com.example.olaznog59.proyectoustvista.modelo.negocio.singletons.Clave;
import com.example.olaznog59.proyectoustvista.modelo.contentprovider.ContactosAgenda;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by olaznog59 on 25/11/2016.
 */

//INICIO----------------------------
//COMPROBAR LAS SHARED PREFERENCES, SI NO HAY INFORMACIÓN, ENVIARÁ A LA PANTALLA DE LOGIN
//SI TENEMOS LOS PARÁMETROS LLAMAMOS AL SERVICIO REST 'obtenerMatches' DE LA CLASE "GestorServer"
// MÉTODO 'obtenerNumeros" DE LA CARPETA PROVEEDOR-> CLASE "ContactosAgenda" tenemos que pasar el contexto para instanciar la clase.
//CUANDO OBTENGAMOS LOS MATCHES, SOLICITAREMOS LAS COORDENADAS DE LOS CONTACTOS Y LLAMARÁ AL MAPA
//PARALELAMENTE SE MANDARÁN LAS COORDENADAS DEL USUARIO
public class SplashActivity extends Activity {

    public static final String NOMBRE_PREFERENCIAS = "datos";
    public static final String TAG = "SplashActivity";
    String phone;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





   }

    @Override
    protected void onResume() {
        super.onResume();
        //primero sacamos las constantes de las shared preferences
        SharedPreferences sp = getSharedPreferences(NOMBRE_PREFERENCIAS, Context.MODE_PRIVATE);
        phone = sp.getString("phone", "");
        key = sp.getString("key", "");

        /*
        //Solicitamos las coordenadas del usuario
        Location loc;
        double latitud = 0;
        double longitud = 0;
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null){
                latitud = loc.getLatitude();
                longitud = loc.getLongitude();
            }
        }


        //cada vez que iniciemos la aplicación mandamos las coordenadas
        //siempre y cuando ya estemos registrados en la aplicación
        GestorServer gs = new GestorServer(this);
        Clave clave = Clave.getInstance();
        if (clave.getPhone() != null && latitud != 0 && longitud != 0){
            gs.enviarCoordenadas(latitud, longitud);
        } */


        Intent alertIntent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,
                alertIntent,
                0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5*1000,
                AlarmManager.INTERVAL_HALF_HOUR,
                pendingIntent);

        Log.d(TAG, "Creada alarma cada media hora para actualizar los datos del servidor");


        //No estoy segura de que se cree la variable al poner "" en el método, por tanto,
        // como el método va a tener al menos nueve carácteres, compruebo si tiene esa longitud
        Clave clave = Clave.getInstance();
        if (phone.length() >= 9) {

            Log.d(TAG, "Obtenidos datos desde la SharedPreferences: " + phone + " " + key);
            clave.inicialice(phone, key);
            /*
            ContactosAgenda contactosAgenda = new ContactosAgenda(this);
            ArrayList<String> contactos = contactosAgenda.obtenerNumeros();
            GestorServer gs = new GestorServer(this);
            gs.obtenerMatches(contactos);*/
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
