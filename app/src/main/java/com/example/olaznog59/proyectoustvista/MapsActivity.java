package com.example.olaznog59.proyectoustvista;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Usuario;
import com.example.olaznog59.proyectoustvista.modelo.negocio.PermissionUtils;
import com.example.olaznog59.proyectoustvista.modelo.entidad.Contacto;
import com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest.GestorServer;
import com.example.olaznog59.proyectoustvista.modelo.negocio.singletons.Clave;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.olaznog59.proyectoustvista.modelo.persistencia.DaoContactos;

import java.util.ArrayList;

import static com.example.olaznog59.proyectoustvista.R.id.nombre;

//Esta app muestra cómo GMS Location puede usarse para cambios en la posición de los usuarios.
//El botón de "Mi localización" usa GMS Location para obtener el punto azul que representa la ubicación de los usuarios.
//Se requieren los permisos "ACCESS_FINE_LOCATION".
//Si los permisos con están concedidos , la actividad finalizará con un mensaje de error.

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,GoogleMap.OnMarkerClickListener{


    private GoogleMap mMap;
    //Booleano que nos indica cuándo un permiso ha sido denegado en el "onRequestPermissionResult"
    private boolean mPermissionDenied = false;
    private LatLng localizacion = new LatLng(40, -4);
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapa);

        //Add toolbar a la actividad
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Eliminamos el titulo de la aplicacion
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Obtenemos el SupportMapFragment y recibimos notificación cuando el mapa está listo para ser usado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor, espera unos segundos.");
        progressDialog.setTitle("Cargando...");
        progressDialog.show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        GestorServer gs = new GestorServer(this);
        gs.comprobarMatches();

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 5));

        try {
            mMap.setMyLocationEnabled(true);
        }catch (SecurityException e){
            Log.d("MapsActivity","Esto no deberia pasar ya que pedimos los permisos antes de entrar");
            e.printStackTrace();
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        //cargarMapa();
        mMap.setOnMyLocationButtonClickListener(this);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(final Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                View view = LinearLayout.inflate(MapsActivity.this,R.layout.layout_marcadores,null);
                TextView tvNombre = (TextView)view.findViewById(nombre);
                TextView tvTelefono = (TextView) view.findViewById(R.id.telefono);
                TextView tvCoordenadas = (TextView) view.findViewById(R.id.coordenadas);

                Usuario usuario = (Usuario)marker.getTag();

                tvNombre.setText(usuario.getNombre());
                tvTelefono.setText(usuario.getPhone());
                String coordenadas = String.valueOf(usuario.getLat())
                        + " - "
                        + String.valueOf(usuario.getLon());
                tvCoordenadas.setText(coordenadas);

                return view;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("MapsActivity","Pulsado el boton llamar");
                Usuario usuario = (Usuario)marker.getTag();
                String uriTelefono = "tel://" + usuario.getPhone();
                llamar(uriTelefono);
            }
        });
    }

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
        if (id == R.id.action_eliminar) {
            Toast.makeText(this,"Ha pulsado 'Darte de baja'",Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);


            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    GestorServer gs = new GestorServer(MapsActivity.this);
                    gs.eliminarUsuario();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Toast.makeText(MapsActivity.this,"Ha cancelado la operación", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        } if (id == R.id.action_salir){
            Toast.makeText(this,"Ha pulsado 'Salir de la App'",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            return true;

        }

        return super.onOptionsItemSelected(item);
    }




    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            //Permisos denegado, mostramos el error en el monitor
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Mi posición", Toast.LENGTH_SHORT).show();
        //Devuelve false así que no consumimos el evento y sigue estando el comportamiento por defecto
        //(El "Camera Animates" va a la posisción actual del usuario)
        return false;
    }

    //Cuando hagamos "click" en un Marker, se lanzará este método

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.d("MapsActivity","onMarkerClick -> entrando");

        return false;
    }

    //Este método llama al contacto correspondiente
    public void llamar (String telefono){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telefono));
        startActivity(intent);
    }

    public void cargarMapa() {
        progressDialog.dismiss();
        mMap.setOnMarkerClickListener(this);

        DaoContactos daoContactos = new DaoContactos(this);
        ArrayList<Usuario> listaUsuarios = daoContactos.obtenerContactos();

        Clave clave = Clave.getInstance();
        if (listaUsuarios != null){
            //ArrayList<Usuario> listaUsuarios = clave.getArrayUsuarios();
            for(Usuario usuario : listaUsuarios){
                LatLng latLng = new LatLng(usuario.getLat(),usuario.getLon());
                String nombre = usuario.getNombre();
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(nombre)
                        .snippet(latLng.toString()));

                marker.setTag(usuario);
            }
        }
    }


}
