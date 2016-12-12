package com.example.olaznog59.proyectoustvista.modelo.broadcasts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.olaznog59.proyectoustvista.modelo.negocio.singletons.Clave;

/**
 * Created by Usuario on 28/11/2016.
 */

public class AlertReceiver extends BroadcastReceiver {
    public static final String TAG = "AlertReceiver";
    double latitud;
    double longitud;
    Clave clave;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Paso por aquí para recibir el AlarmManager");
        SendCoordinatesService.startActionSendCoordinates(context);

    }


}
