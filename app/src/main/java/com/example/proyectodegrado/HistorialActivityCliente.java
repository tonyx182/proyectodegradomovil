package com.example.proyectodegrado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.proyectodegrado.Adapter.AdapterCliente;
import com.example.proyectodegrado.Adapter.CustomAdapter;
import com.example.proyectodegrado.Adapter.ItemClickListener;
import com.example.proyectodegrado.api.RetrofitClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialActivityCliente extends AppCompatActivity {

    List<Carreras> lista;
    RecyclerView recyclerView;
    EditText dateStart, dateEnd;
    String fechaInicio, fechaFin;
    Button btnAceptarFechas;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cliente);
        dateStart = findViewById(R.id.dateStart);
        dateEnd = findViewById(R.id.dateEnd);
        btnAceptarFechas = findViewById(R.id.btnAceptarFecha);
        Date c = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -15);
        Date inicio = calendar.getTime();;
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        fechaFin = df.format(c);
        fechaInicio = df.format(inicio);

        btnAceptarFechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historial(fechaInicio, fechaFin);
            }
        });

        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });
        dateEnd.setText(fechaFin);
        dateStart.setText(fechaInicio);

        historial(fechaInicio, fechaFin);
        
        recyclerView = findViewById(R.id.vistaHistorialCliente);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



    }

    private void historial(String inicio, String fin) {
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        idUsuario = preferences.getInt("idUsuario", 0);
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idUsuario));
        RequestBody start = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(inicio));
        RequestBody end = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(fin));

        RetrofitClient retrofitClient = new RetrofitClient();
        Call<List<Carreras>> call = retrofitClient.service.carreras(idUser, start, end);
        call.enqueue(new Callback<List<Carreras>>() {
            @Override
            public void onResponse(Call<List<Carreras>> call, Response<List<Carreras>> response) {
                Log.e("TAG", "onResponse: " + response.body());
                lista = response.body();
                AdapterCliente adapterCliente = new AdapterCliente(lista, new ItemClickListener() {
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
                recyclerView.setAdapter(adapterCliente);
                adapterCliente.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Carreras>> call, Throwable t) {

            }
        });
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + month + "-" + dayOfMonth;
                        dateStart.setText(date);
                        fechaInicio = date;
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR)-1,
                0,
                1
        );
        datePickerDialog.show();
    }

    private void showDatePickerDialog2() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + month + "-" + dayOfMonth;
                        dateEnd.setText(date);
                        fechaFin = date;
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR)-0,
                Calendar.getInstance().get(Calendar.MONTH)-0,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-0
        );
        datePickerDialog.show();
    }
}