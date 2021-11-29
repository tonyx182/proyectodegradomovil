package com.example.proyectodegrado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivityTaxista extends AppCompatActivity {

    CardView cardCarreras;
    CardView cardHistorial;
    CardView cardCerar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_taxista);

        cardCarreras = findViewById(R.id.cardCarreras);
        cardHistorial = findViewById(R.id.cardHistorial);
        cardCerar = findViewById(R.id.cardCerrar);

        cardCarreras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SolicitudesActivity.class);
                startActivity(intent);
            }
        });
        cardHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialActivityTaxista.class);
                startActivity(intent);
            }
        });
        cardCerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}