package com.example.proyectodegrado.api;

import retrofit2.Retrofit;

public class RetrofitClient {

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost/codeigniter/proyectodegrado/index.php/restserver/")
            .build();

    public ApiService service = retrofit.create(ApiService.class);
}

