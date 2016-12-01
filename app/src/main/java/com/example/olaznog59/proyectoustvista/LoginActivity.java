package com.example.olaznog59.proyectoustvista;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.olaznog59.proyectoustvista.modelo.broadcasts.AlertReceiver;
import com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest.GestorServer;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    public static final String NOMBRE_PREFERENCIAS = "datos";
    public static final String TAG = "LoginActivity";


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private EditText mMovilView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Add el toolbar a la actividad
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Eliminamos el titulo de la aplicacion

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        // Set up the login form.
        mMovilView = (EditText) findViewById(R.id.movil);

        mayRequestPermission();
    }

    public void buttonEnviarNumero (View v){

        String sTlf = mMovilView.getText().toString();

        if (sTlf.length() >= 9){
            GestorServer gs = new GestorServer(this);
            gs.obtenerIdUsuario(sTlf);

        } else {
            Toast.makeText(this, "El número introducido no es correcto", Toast.LENGTH_SHORT).show();
        }

    }

    private void mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
        }else{
            requestPermissions(new String[]{READ_CONTACTS,ACCESS_FINE_LOCATION,WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            }else{
                Toast.makeText(this,"Debe aceptar los permisos",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


/*
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this,"Ha pulsado 'Settings'",Toast.LENGTH_SHORT).show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
*/




}

