package com.example.olaznog59.proyectoustvista.modelo.negocio.serviciosrest;



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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Usuario on 23/11/2016.
 */

//-----------------------------------------------------------------------------------------------
    //PREGUNTAR A RICARD POR LO QUE NOS DEVUELVE EL CONTENT PROVIDER, ARRAY // ARRAYLIST??

public interface ServicioRest {

    //PARA AÑADIR LA EXTENSIÓN EL URL DEL OBJETO "RETROFIT" TIENE QUE ACABAR CON "/" PARA QUE SUME AL STRING CREADO
    //SI NO TIENE EL "/" SUSTITUIRÁ LA ÚLTIMA PARTE DE LA URL

    //PARA OBTENER EL ID DEL USUARIO
    @POST("register_user")
    Call<Register> getId(@Body ToRegister register);

    //PARA OBTENER LOS IDS DE NUESTRA AGENDA
    @POST("get_contacts")
    Call<Matches> getMatches(@Body Contact contact);

    //PARA MANDAR NUESTRA UBICACIÓN
    @POST("update_coordinates")
    Call<SendCoord> sendCoord(@Body ToSendCoord coord);

    //PARA OBTENER COORDENADAS DE TODOS LOS USUARIOS DE MI AGENDA
    @POST("get_coordinates")
    Call<RespuestaServidor> getCoord(@Body ToGetCoord toGetCoord);


    @POST("delete_user")
    Call<Deleted> toUnregister(@Body ToDelete toDelete);

}
