package com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.olaznog59.proyectoustvista.LoginActivity;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request.Contact;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request.ToDelete;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request.ToGetCoord;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request.ToRegister;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.request.ToSendCoord;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Deleted;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.RespuestaServidor;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Matches;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Register;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.SendCoord;
import com.example.olaznog59.proyectoustvista.MapsActivity;
import com.example.olaznog59.proyectoustvista.modelo.entidad.servicios.response.Usuario;
import com.example.olaznog59.proyectoustvista.modelo.persistencia.DaoContactos;
import com.example.olaznog59.proyectoustvista.modelo.negocio.singletons.Clave;
import com.example.olaznog59.proyectoustvista.modelo.contentprovider.ContactosAgenda;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Usuario on 24/11/2016.
 */


//JUNTANDO PARTES EN EL REPOSITORIO DE GITHUB
    //--------------------------------------------------------------------------------------------------------

public class GestorServer {

    public static final String url = "http://ec2-52-19-159-183.eu-west-1.compute.amazonaws.com:8088/talentumgeo/rest/services/";
    public static final String TAG = "GestorServer";
    private Clave clave;

    RespuestaServidor objetoConCoordenadas = new RespuestaServidor();
    Activity activity;

    public GestorServer(Activity activity){
        this.activity = activity;
    }


    public void obtenerIdUsuario(final String telefono){
        clave = Clave.getInstance();
        Log.d(TAG,"obtenerIdUsuario -> Vamos a registrar el tlf: "+telefono);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) //URL A CONECTARSE
                .addConverterFactory(GsonConverterFactory.create()) //FORMATO DE "SERIALIZACIÓN"
                .build();

        ServicioRest service = retrofit.create(ServicioRest.class); //OBJETO DE LA INTERFAZ

        ToRegister register = new ToRegister(); //OBJETO A SERIALIZAR PARA EL "BODY"
        register.setPhone(telefono);
        Log.d(TAG,"obtenerIdUsuario -> Se ha generado el objeto 'ToRegister': "+register.toString());

        Call<Register> call = service.getId(register); //MÉTODO A LLAMAR
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> registro) {

                //whether the response is be able to parse or not, onResponse will be always called.
                // But in the case the result couldn't be parsed into the Object, response.body() will return as null.
                //En caso de recibir un error, código 404... recibiremos un objeto no serializable
                String str = "";
                try {
                    str = registro.errorBody().toString();
                    Log.d(TAG,"obtenerIdUsuario -> Error: " + str);
                } catch (Exception e){
                    Log.d(TAG,"obtenerIdUsuario -> Excepción: " + e);
                }
                Log.d(TAG,"obtenerIdUsuario -> Se ha realizado la llamada con éxito, el objeto recibido: " + registro.toString());
                Register reg =  registro.body();

                Log.d(TAG,"obtenerIdUsuario -> " + reg.toString());

                if (reg.getErrorCode()==0){
                    //AQUÍ TENDREMOS QUE GUARDAR EL TLF Y LA KEY EN SHARED PREFERENCES Y INICIAR EL OBJETO "CLAVES"
                    Log.d(TAG,"obtenerIdUsuario -> Se ha generado el objeto Clave");
                    clave.inicialice(telefono,reg.getKey());

                    SharedPreferences sp = activity.getSharedPreferences(LoginActivity.NOMBRE_PREFERENCIAS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    //guardamos el teléfono y la llave en la shared preferences
                    editor.putString("phone",clave.getPhone());
                    editor.putString("key",clave.getKey());
                    editor.commit();

                    String telefonoSP = sp.getString("phone" ,"");
                    String keySP = sp.getString("key","");
                    Log.d(TAG, "obtenerIdUsuario -> obtenido de sharedpreferences " + telefonoSP + " " + keySP);

                    //cogemos los contactos de la agenda y los mandamos al servicioREST
                    /*
                    ContactosAgenda contactosAgenda = new  ContactosAgenda(activity);
                    ArrayList<String>  contactos = contactosAgenda.obtenerNumeros();
                    obtenerMatches(contactos);*/
                    Intent intent = new Intent(activity,MapsActivity.class);
                    activity.startActivity(intent);
                } else {
                    //Aquí deberíamos mostrar un toast, posiblemente mandando un código a la pantalla para que vuelva a pedir el telefono.
                    Toast.makeText(activity, "Error: " + reg.getErrorDescription() , Toast.LENGTH_SHORT).show();
                    Log.d(TAG ,"obtenerIdUsuario -> Ha habido un error en el registro: "+reg.getErrorDescription());
                }

            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(activity, "Se ha producido un error al enviar los contactos, no hay servicio de internet", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"obtenerIdUsuario -> Ha fallado la conexión con el servidor: "+t.getMessage());
            }
        });
    }

    //COMO EL OBJETO CLAVES LO NECESITAMOS EN ÉSTA CLASE, LO GENERAREMOS AQUÍ
    //CADA VEZ QUE INICIALICEMOS EL PROGRAMA PEDIREMOS LOS "MATCHES" AL SERVIDOR, POR TANTO,
    //CONFIGURAMOS PARA QUE CUANDO SOLICITEMOS LOS MATCHES SE GENERE EL OBJETO CLAVES

    public void obtenerMatches (ArrayList<String> contacts){

        clave = Clave.getInstance();
        Log.d(TAG ,"obtenerMatches -> Vamos a solicitar los usuarios registrados en la app P: "+clave.getPhone());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicioRest servicioRest = retrofit.create(ServicioRest.class);

        Log.d(TAG,"obtenerMatches -> Creado ServicioRest y Retrofit");
        //generamos objeto solicitado por el servidor
        Contact contact = new Contact();
        contact.setPhone(clave.getPhone());
        contact.setKey(clave.getKey());
        contact.setContacts(contacts);

        Log.d(TAG,"obtenerMatches -> Objeto a mandar: "+ contact.toString());

        Call<Matches> call = servicioRest.getMatches(contact);

        call.enqueue(new Callback<Matches>() {
            @Override
            public void onResponse(Call<Matches> call, Response<Matches> response) {
                Log.d(TAG,"obtenerMatches -> Se ha obtenido el siguiente objeto: " +  response.body().toString());
                //Aquí guardaremos los matches recibidos en el objeto claves
                Matches matches = response.body();
                if (matches.getError_code() == 0){
                    //si se ha realizado la operación con éxito, se guardarán los matches
                    //En este caso el objeto "Claves" estará referenciado aquí y donde lanzamos la petición

                    Log.d(TAG,"obtenerMatches -> Se han guardado los matches en el objeto Clave");
                    if (matches.getMatches().size()>0){
                        clave.setMatches(matches.getMatches()); // guardamos en el objeto CLAVES los valores recibidos
                        obtenerCoordenadas();
                    } else {
                        //Mostramos un "Toast" al usuario para que sepa lo que ha pasado
                        Toast.makeText(activity, "Todavía no tiene ningún contacto registrado en la aplicación",Toast.LENGTH_LONG).show();
                        //Pasamos al mapa, que es la siguiente actividad
                        /*Intent intent = new Intent(activity, MapsActivity.class);
                        activity.startActivity(intent);*/
                        MapsActivity mapsActivity = (MapsActivity)activity;
                        mapsActivity.cargarMapa();
                    }

                } else {
                    Toast.makeText(activity,"Error " + matches.getError_description(),Toast.LENGTH_LONG).show();
                    MapsActivity mapsActivity = (MapsActivity)activity;
                    mapsActivity.cargarMapa();
                }


            }

            @Override
            public void onFailure(Call<Matches> call, Throwable t) {
                Toast.makeText(activity, "Se ha producido un error al enviar los contactos, no hay servicio de internet", Toast.LENGTH_LONG).show();
                Log.d(TAG,"obtenerMatches -> Something happened: " + t.getMessage());
                MapsActivity mapsActivity = (MapsActivity)activity;
                mapsActivity.cargarMapa();
            }
        });

    }



    public void enviarCoordenadas (double lat, double lon){
        clave = Clave.getInstance();

        Log.d(TAG,"enviarCoordenadas -> Vamos a mandar coordenadas Lat: " + lat + " Lon: " + lon);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Creamos objeto a mandar
        ToSendCoord toSendCoord = new ToSendCoord();
        toSendCoord.setPhone(clave.getPhone());
        toSendCoord.setKey(clave.getKey());
        toSendCoord.setLat(lat);
        toSendCoord.setLon(lon);

        Log.d(TAG,"enviarCoordenadas -> Mandando petición con: "+ toSendCoord.toString());

        ServicioRest servicioRest = retrofit.create(ServicioRest.class);

        Call <SendCoord> call = servicioRest.sendCoord(toSendCoord);

        call.enqueue(new Callback<SendCoord>() {
            @Override
            public void onResponse(Call<SendCoord> call, Response<SendCoord> response) {
                String str = "";
                try {
                    str = response.errorBody().toString();
                    Log.d(TAG,"enviarCoordenadas -> Error: "+str);
                } catch (Exception e){
                    Log.d(TAG,"enviarCoordenadas -> Excepción: " + e);
                }
                //AQUÍ NO TENEMOS QUE HACER NADA MÁS.... ????
                SendCoord sendCoord = (SendCoord) response.body();
//-----------------------  ALGUNA COMPROBACIÓN  ------------------------- ????
                Log.d(TAG,"enviarCoordenadas -> Respuesta petición: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<SendCoord> call, Throwable t) {

                Log.d(TAG,"enviarCoordenadas -> Something happened: " + t.getMessage());
            }
        });
    }


    //último método para poder representar las coordenadas

//----------------- SI FUNCIONA COMO LO PREVISTO, LOS MATCHES SE GENERARÁN AL INICIAR LA APP ----------
    // -------- POR TANTO NO NECESITAMOS MANDAR NADA PARA OBTENERLOS


    public void obtenerCoordenadas(){

        clave = Clave.getInstance();
        Log.d(TAG,"obtenerCoordenadas -> Mandando petición de coordenadas de los usuarios");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //OBJETO A MANDAR
        //debe ser independiente al generado "patrón singleton" para que se pueda serializar
        ToGetCoord toGetCoord = new ToGetCoord();
        toGetCoord.setPhone(clave.getPhone());
        toGetCoord.setKey(clave.getKey());
        toGetCoord.setMatches(clave.getMatches());

        Log.d(TAG,"obtenerCoordenadas -> Mandando petición con: " + toGetCoord.toString());

        ServicioRest servicioRest = retrofit.create(ServicioRest.class);

        Call <RespuestaServidor> call = servicioRest.getCoord(toGetCoord);


        call.enqueue(new Callback<RespuestaServidor>() {
            @Override
            public void onResponse(Call<RespuestaServidor> call, Response<RespuestaServidor> response) {
                String str = "";
                try {
                    str = response.errorBody().toString();
                    Log.d(TAG,"obtenerCoordenadas -> Error: " + str);
                } catch (Exception e){
                    Log.d(TAG,"obtenerCoordenadas -> Excepción: " + e);
                }
                //en caso de haber realizado la petición con éxito enviaremos el array de usuarios
                objetoConCoordenadas = (RespuestaServidor) response.body();

                //como es una fase de pruebas, vamos a comprobar los datos recibidos
                Object oResponse = response.body();
                if (oResponse != null && objetoConCoordenadas.getErrorCode() == 0){
                    Log.d(TAG,"obtenerCoordenadas -> Recibida respuesta: " + response.body().toString());
                    Log.d(TAG,"obtenerCoordenadas -> Guardados los usuarios de la aplicación");
                    Log.d(TAG, "obtenerCoordenadas -> Obtenidos Array de Usuarios de la aplicación");

                    ContactosAgenda contactosAgenda = new ContactosAgenda(activity);
                    DaoContactos almacenContactos = new DaoContactos(activity);
                    ArrayList<Usuario> users = objetoConCoordenadas.getCoordinates();
                    clave.setArrayUsuarios(users);
                    for(Usuario usuario :users){
                        contactosAgenda.obtenerNombres(usuario);
                        Log.d(TAG,"obtenerCoordenadas -> Usuario con nombre: "+usuario.toString());
                        almacenContactos.crearContacto(usuario);
                    }

/*
                    Intent intent = new Intent(activity, MapsActivity.class);
                    //intent.putExtra("Usuarios", objetoConCoordenadas.getCoordinates());
                    activity.startActivity(intent);*/

                    //activity.finish();
                }else{
                    Toast.makeText(activity,"Algun error ha ocurrido: " + objetoConCoordenadas.getErrorDescription(),Toast.LENGTH_SHORT).show();
                }

                MapsActivity mapsActivity = (MapsActivity)activity;
                mapsActivity.cargarMapa();

            }

            @Override
            public void onFailure(Call<RespuestaServidor> call, Throwable t) {

                Toast.makeText(activity,"Ha habido un error de conexión con el servidor", Toast.LENGTH_LONG).show();
                //Aquí deberíamos mandar a la actividad del mapa y que recupere los contactos de la agenda
                Log.d(TAG,"obtenerCoordenadas -> Something happened: " + t.getMessage());
                MapsActivity mapsActivity = (MapsActivity)activity;
                mapsActivity.cargarMapa();
            }
        });
    }


    public void eliminarUsuario() {

        Log.d(TAG,"eliminarUsuario -> Se va a eliminar el usuario");
        Clave clave = Clave.getInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ToDelete toDelete = new ToDelete();
        toDelete.setPhone(clave.getPhone());
        toDelete.setKey(clave.getKey());


        ServicioRest servicioRest = retrofit.create(ServicioRest.class);

        Call<Deleted> call = servicioRest.toUnregister(toDelete);

        call.enqueue(new Callback<Deleted>() {
            @Override
            public void onResponse(Call<Deleted> call, Response<Deleted> response) {
                Deleted deleted = response.body();

                if (deleted.getErrorCode() == 0){
                    SharedPreferences sp = activity.getSharedPreferences(LoginActivity.NOMBRE_PREFERENCIAS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    //guardamos el teléfono y la llave en la shared preferences
                    editor.putString("phone","");
                    editor.putString("key","");
                    editor.commit();
                }
                Toast.makeText(activity, deleted.getErrorDescription(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Deleted> call, Throwable t) {

                Toast.makeText(activity, "Ha habido un error con la conexión al servidor", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void comprobarMatches(){
        ContactosAgenda contactosAgenda = new ContactosAgenda(activity);
        ArrayList<String> contactos = contactosAgenda.obtenerNumeros();
        GestorServer gs = new GestorServer(activity);
        gs.obtenerMatches(contactos);
    }

}
