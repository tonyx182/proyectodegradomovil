package com.example.proyectodegrado.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.2/codeigniter/proyectodegrado/index.php/restserver/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ApiService service = retrofit.create(ApiService.class);
}

