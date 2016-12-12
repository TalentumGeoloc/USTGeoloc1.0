package com.example.olaznog59.proyectoustvista.modelo.broadcasts;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.olaznog59.proyectoustvista.R;
import com.example.olaznog59.proyectoustvista.SplashActivity;
import com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest.GestorServer;
import com.example.olaznog59.proyectoustvista.modelo.negocio.singletons.Clave;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class SendCoordinatesService extends IntentService {

    public static final String TAG = "SendCoordinatesService";
    private static final String ACTION_SEND_COORDINATES = "com.example.olaznog59.proyectoustvista.AlarmManager.action.SEND_COORDINATES";

    private static final String EXTRA_LAT = "com.example.olaznog59.proyectoustvista.AlarmManager.extra.LAT";
    private static final String EXTRA_LON = "com.example.olaznog59.proyectoustvista.AlarmManager.extra.LON";
    private static final double BAD_COORDINATE = -999.9;    //número fuera del rango posible de coordenadas
                                                            //con el que determino que una coordenada no es buena
                                                            //o no existe

    private static final String NOMBRE_PREFERENCIAS = "datos";
    private static final String PHONE_FIELD = "phone";
    private static final String KEY_FIELD = "key";

    private static double latitud;
    private static double longitud;
    private Clave clave;

    public SendCoordinatesService() {
        super("SendCoordinatesService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionSendCoordinates(Context context) {
        // TODO: 1/12/16 SACAR LAS COORDENADAS AQUÍ
        //Solicitamos las coordenadas del usuario
        Location loc = null;
        LocationManager locManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //NO TIENE PERMISOS. Aquí no se hace nada, porque esto es un intent service.
                //Los permisos hay que pedirselos cuando arranque la app por primera vez, o cuando se establezca
                //el alarmmanager...
                return;
            } else {
                Log.d(TAG, "Permisos necesarios OK!.");
                loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        } else {
            Log.d(TAG, "Versión de SDK inferior a la 6.0, los permisos ya se dieron cuando se instalo la aplicación");
            loc =locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }

        if(loc != null) {
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
        }else{
            return;
        }

        Intent intent = new Intent(context, SendCoordinatesService.class);
        Log.d(TAG,"Preparando para mandar IntentService");
        intent.setAction(ACTION_SEND_COORDINATES);
        intent.putExtra(EXTRA_LAT, latitud);
        intent.putExtra(EXTRA_LON, longitud);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_COORDINATES.equals(action)) {
                final double lat = intent.getDoubleExtra(EXTRA_LAT, BAD_COORDINATE);
                final double lon = intent.getDoubleExtra(EXTRA_LON, BAD_COORDINATE);
                if(lat != BAD_COORDINATE && lon != BAD_COORDINATE) {
                    Log.d(TAG, "Mandando coordenadas " + lat + " " + lon);
                    //si no llegan las coordenadas en el intent, inicializa las variables a
                    //BAD_COORDINATE, por tanto en ese caso no hago nada
                    handleActionSendCoordinates(lat, lon);
                }else{
                    Log.d(TAG,"No se han recuperado bien los valores de las coordenadas");
                }
            }
        }
    }

    private void handleActionSendCoordinates(double lat, double lon) {
        this.latitud = lat;
        this.longitud = lon;
        //leo los valores de las preferencias
        SharedPreferences sp = this.getSharedPreferences(NOMBRE_PREFERENCIAS, Context.MODE_PRIVATE);
        String phone = sp.getString(PHONE_FIELD, "");
        String key = sp.getString(KEY_FIELD, "");
        Log.d(TAG,"Recuperados los valores de las sharedPreferences");
        if (phone.equals("") || key.equals("")){
            //No hay ningún dato, no hago nada porque el usuario ni siguiera ha hecho login
            //// TODO: 1/12/16 manejar error correspondiente
            Log.d(TAG,"El usuario todavía no se ha logeado");
            return;
        }
        clave = Clave.getInstance();
        clave.inicialice(phone,key);

        Log.d(TAG, "Recibida latitud, longitud y objeto clave" + latitud + " " + longitud + " " + clave.getPhone());

        GestorServer gestorServer = new GestorServer(null);
        gestorServer.enviarCoordenadas(latitud,longitud);

        /*if (i<0){
            Log.d(TAG, "Ha habido un problema con la conexión a internet");
        } else if (i==0){
            Log.d(TAG,"Se han enviado las coordenadas satisfactoriamente");
        }
*/



        crearNotificacion(this);
    }

    public void crearNotificacion(Context context){

        PendingIntent notifIntent = PendingIntent.getActivity(context, 0,
                new Intent(context,SplashActivity.class), 0);
        Log.d("AlarmReceiver","Ha entrado en el método 'crearNotif'");
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle("Se han mandado las coordenadas del tlf: "+clave.getPhone())
                .setTicker("Alert")
                .setContentText("Enviadas coordenadas: " + latitud + " " + longitud)
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
