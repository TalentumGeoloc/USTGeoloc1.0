package com.example.olaznog59.proyectoustvista.modelo.broadcasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class MyOnBootCompletedReceiver extends BroadcastReceiver {

    public static final String TAG = "MyOnBootCompletedReceiver";

    public MyOnBootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Entra por aquí para realizar las operaciones del AlarmManager");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SendCoordinatesService.startActionSendCoordinates(context);

            //Cuando el dispositivo se reinicie, debemos programar otra vez la alarma para que
            ////se envien las coordenadas cada meida hora, sin necesidad de acceder a la aplicación

            Intent alertIntent = new Intent(context, SendCoordinatesService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context,
                    0,
                    alertIntent,
                    0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                    AlarmManager.INTERVAL_HALF_HOUR,
                    pendingIntent);
        }
    }
}
