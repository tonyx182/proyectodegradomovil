package com.example.proyectodegrado.api;

import com.example.proyectodegrado.Carreras;
import com.example.proyectodegrado.ResponseSolicitud;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("solicitar")
    Call<ResponseSolicitud> solicitarViaje(
            @Part("idUsuario") RequestBody idUsuario,
            @Part("latitudOrigen") RequestBody latitudOrigen,
            @Part("longitudOrigen") RequestBody longitudOrigen,
            @Part("latitudDestino") RequestBody latitudDestino,
            @Part("longitudDestino") RequestBody longitudDestino,
            @Part("descripcion") RequestBody descripcion
    );

    @GET("solicitudes")
    Call<List<Carreras>> solicitarCarreras(

    );

    @Multipart
    @POST("responder")
    Call<String> responderSolicitud(
            @Part("idServicio") RequestBody idServicio,
            @Part("idUsuario1") RequestBody idUsuario1,
            @Part("carreraAceptada") RequestBody carreraAceptada,
            @Part("correo") RequestBody correo,
            @Part("costo") RequestBody costo

    );

    @Multipart
    @POST("historial2")
    Call<List<Carreras>> carreras(
            @Part("idUsuario") RequestBody idUsuario,
            @Part("dateStart") RequestBody dateStart,
            @Part("dateEnd") RequestBody dateEnd
    );

    @Multipart
    @POST("historialTaxista")
    Call<List<Carreras>> carrerasTaxista(
            @Part("idUsuario1") RequestBody idUsuario1
    );
}
