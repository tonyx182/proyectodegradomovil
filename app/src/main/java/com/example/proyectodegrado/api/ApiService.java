package com.example.proyectodegrado.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("solicitar")
    Call solicitarViaje(
            @Part("idUsuario") RequestBody idUsuario,
            @Part("latitudOrigen") RequestBody latitudOrigen,
            @Part("longitudOrigen") RequestBody longitudOrigen,
            @Part("latitudDestino") RequestBody latitudDestino,
            @Part("longitudDestino") RequestBody longitudDestino
    );
}
