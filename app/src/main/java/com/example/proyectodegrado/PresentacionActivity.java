package com.example.proyectodegrado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class PresentacionActivity extends AppCompatActivity {

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion", false);
                int idRol = preferences.getInt("idRol", -1);
                Intent intent;
                switch (idRol) {
                    case 2: intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 3: intent = new Intent(getApplicationContext(),MainActivityTaxista.class);
                        startActivity(intent);
                        finish();
                        break;
                    default: intent = new Intent(getApplicationContext(), com.example.proyectodegrado.Login.class);
                        startActivity(intent);
                        finish();
                }

                /*if (sesion){
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), com.example.proyectodegrado.Login.class);
                    startActivity(intent);
                    finish();
                }*/
            }
        }, 2000);
    }
}