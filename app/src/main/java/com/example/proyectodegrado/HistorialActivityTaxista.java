package com.example.proyectodegrado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectodegrado.Adapter.AdapterCliente;
import com.example.proyectodegrado.Adapter.AdapterTaxista;
import com.example.proyectodegrado.Adapter.ItemClickListener;
import com.example.proyectodegrado.api.RetrofitClient;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialActivityTaxista extends AppCompatActivity {

    List<Carreras> lista;
    RecyclerView recyclerView;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_taxista);

        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("idUsuario", 0);
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idUsuario));

        recyclerView = findViewById(R.id.vistaHistorialTaxista);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        RetrofitClient retrofitClient = new RetrofitClient();
        Call<List<Carreras>> call = retrofitClient.service.carrerasTaxista(idUser);
        call.enqueue(new Callback<List<Carreras>>() {
            @Override
            public void onResponse(Call<List<Carreras>> call, Response<List<Carreras>> response) {
                Log.e("TAG", "onResponse: " + response.body());
                lista = response.body();
                AdapterTaxista adapterTaxista = new AdapterTaxista(lista, new ItemClickListener() {
                    @Override
                    public void onClickItem(Carreras carrera) {
                        Log.e("TAG", "onClickItem: " + carrera.latitudDestino);
                        Intent intent = new Intent(getApplicationContext(),MapsActivityHistorial.class);
                        intent.putExtra("latOri", carrera.latitudOrigen);
                        intent.putExtra("longOri", carrera.longitudOrigen);
                        intent.putExtra("latDes", carrera.latitudDestino);
                        intent.putExtra("longDes", carrera.longitudDestino);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapterTaxista);
                adapterTaxista.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Carreras>> call, Throwable t) {

            }
        });
    }
}