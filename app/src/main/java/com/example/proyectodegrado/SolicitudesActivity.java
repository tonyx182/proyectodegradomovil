package com.example.proyectodegrado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectodegrado.Adapter.CustomAdapter;
import com.example.proyectodegrado.Adapter.ItemClickListener;
import com.example.proyectodegrado.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudesActivity extends AppCompatActivity {

    List<Carreras> lista;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        recyclerView = findViewById(R.id.vistaSolicitudes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        RetrofitClient retrofitClient = new RetrofitClient();
        Call<List<Carreras>> call = retrofitClient.service.solicitarCarreras();
        call.enqueue(new Callback<List<Carreras>>() {
            @Override
            public void onResponse(Call<List<Carreras>> call, Response<List<Carreras>> response) {
                Log.e("TAG", "onResponse: " + response.body());
                lista = response.body();
                CustomAdapter customAdapter = new CustomAdapter(lista, new ItemClickListener() {
                    @Override
                    public void onClickItem(Carreras carrera) {
                        Log.e("TAG", "onClickItem: " + carrera.latitudDestino);
                        Intent intent = new Intent(getApplicationContext(),MapsActivityTaxista.class);
                        intent.putExtra("latOri", carrera.latitudOrigen);
                        intent.putExtra("longOri", carrera.longitudOrigen);
                        intent.putExtra("latDes", carrera.latitudDestino);
                        intent.putExtra("longDes", carrera.longitudDestino);
                        intent.putExtra("idServicio", carrera.idServicio);
                        intent.putExtra("corre", carrera.correo);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Carreras>> call, Throwable t) {

            }
        });

    }


}