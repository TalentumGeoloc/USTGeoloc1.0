package com.example.olaznog59.proyectoustvista.modelo.broadcasts;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.olaznog59.proyectoustvista.R;
import com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest.GestorServer;
import com.example.olaznog59.proyectoustvista.SplashActivity;
import com.example.olaznog59.proyectoustvista.modelo.negocio.singletons.Clave;

/**
 * Created by Usuario on 28/11/2016.
 */

public class AlertReceiver extends BroadcastReceiver {
    public static final String TAG = "AlertReceiver";
    double latitud = 0;
    double longitud = 0;
    Clave clave;
    /*
    * INDICACIONES:
    * -Si el dispositivo se reinicio, recoges el evento, mandas las coordenadas y estableces la
    *   alarma siguiente para dentro de una hora
    * -Cuando llegue el evento de alarma (porque haya pasado la hora), mando coordenadas y
    *   establezco una nueva alarma para dentro de otra hora.
    * -Cuando arranco la app, veo si hay una alarma programada (porque se pueda ver directamente,
    *   o porque vayas almacenando tú en una variable que hay una alarma)
    *  Si ya hay una programada, no hago nada. SI no, mando coordenadas, y programo siguiente alarma.
    * -O a lo mejor hay alguna manera de no tener que programar la siguiente alarma to_do el rato.
    *
    * */

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlertReceiver","OnReceive -> Arrancando");
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null){
                latitud = location.getLatitude();
                longitud = location.getLongitude();
            } else {

            }
        }
        else {
            return;
        }

        clave = Clave.getInstance();
        if ( clave.getPhone().length()<9){
            Log.d(TAG,"No ha persistido la variable clave");
            SharedPreferences sp = context.getSharedPreferences(SplashActivity.NOMBRE_PREFERENCIAS, Context.MODE_PRIVATE);
            String phone = sp.getString("phone", "");
            String key = sp.getString("key", "");
            if (phone.length()>=9){
                clave.inicialice(phone,key);
            } else {
                Log.d(TAG, "Las SharedPreferences no contienen datos");
            }
        }

        Log.d(TAG, "Recibida latitud, longitud y objeto clave" + latitud + longitud + clave.getPhone());

        if (latitud == 0 && longitud == 0){
            Log.d(TAG,"No se ha recibido localización");
        } else {
            if (clave.getPhone() != null){
                GestorServer gestorServer = new GestorServer(null);
                gestorServer.enviarCoordenadas(latitud,longitud);
            }
        }

        crearNotificacion(context);

    }

    public void crearNotificacion(Context context){

        PendingIntent notifIntent = PendingIntent.getActivity(context, 0,
                new Intent(context,SplashActivity.class), 0);
        Log.d("AlarmReceiver","Ha entrado en el método 'crearNotif'");
        NotificationCompat.Builder mBuilder;
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle("Se han mandado las coordenadas")
                .setTicker("Alert")
                .setContentText( latitud + " " + longitud)
                .setSmallIcon(R.drawable.plainicon);

        mBuilder.setContentIntent(notifIntent);

        //podemos poner alertas al "device" en este caso incluimos sonido
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        //cancel the notification when is taped in the "device"
        mBuilder.setAutoCancel(true);

        //Notification to user of this background event
        NotificationManager mNotificMngr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //psot notification on the screen
        mNotificMngr.notify(1, mBuilder.build());


    }

}
