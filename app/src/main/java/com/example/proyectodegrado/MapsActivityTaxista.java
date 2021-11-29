package com.example.proyectodegrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectodegrado.api.RetrofitClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MapsActivityTaxista extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap nMap;
    double latitudOrigen;
    double longitudOrigen;
    double latitudDestino;
    double longitudDestino;
    int idServicio;
    double costo;
    String costoLit;
    String correo;
    Button aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_taxista);
        latitudOrigen = getIntent().getExtras().getDouble("latOri");
        longitudOrigen = getIntent().getExtras().getDouble("longOri");
        latitudDestino = getIntent().getExtras().getDouble("latDes");
        longitudDestino = getIntent().getExtras().getDouble("longDes");
        idServicio = getIntent().getExtras().getInt("idServicio");
        correo = getIntent().getExtras().getString("corre");

        aceptar = findViewById(R.id.btnAceptar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapTaxista);
        mapFragment.getMapAsync(this);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                int idUsuario = preferences.getInt("idUsuario", 0);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivityTaxista.this);
                dialog.setTitle("Confirmación");
                dialog.setMessage("Esta seguro de aceptar la solicitud??" + "\n" + "\n" + "Ingrese un costo para el servicio");

                final EditText input = new EditText(MapsActivityTaxista.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setLayoutParams(lp);
                dialog.setView(input);
                dialog.setIcon(R.drawable.ic_costo);

                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        costo = Double.parseDouble(input.getText().toString());
                        costoLit = input.getText().toString();
                        RequestBody idServ= RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idServicio));
                        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idUsuario));
                        RequestBody carrera = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(1));
                        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), correo);
                        RequestBody cost = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(costo));
                        if(!costoLit.isEmpty()) {
                            RetrofitClient client = new RetrofitClient();
                            Call<String> call = client.service.responderSolicitud(idServ, idUser, carrera, email, cost);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                                    Intent intent = new Intent(MapsActivityTaxista.this, MainActivityTaxista.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(MapsActivityTaxista.this, "Usted Acepto de manera correcta la solicitud", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e("TAG", "onFailure: " + t );
                                }
                            });

                        } else {
                            Toast.makeText(MapsActivityTaxista.this, "Debe ingresar una descripcion", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create();
                dialog.show();



            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        nMap = googleMap;
        nMap.addMarker(new MarkerOptions().position(new LatLng(latitudOrigen, longitudOrigen)));
        LatLng markerPointOrigin = new LatLng(latitudOrigen, longitudOrigen);
        nMap.addMarker(new MarkerOptions().position(markerPointOrigin)).setTitle("Origen");
        nMap.addMarker(new MarkerOptions().position(new LatLng(latitudDestino, longitudDestino)));
        LatLng markerPointDestiny = new LatLng(latitudDestino, longitudDestino);
        nMap.addMarker(new MarkerOptions().position(markerPointDestiny)).setTitle("Destino");
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudOrigen, longitudOrigen), 15));
        dibujarRuta(getDirectionsUrl());
    }

    private void dibujarRuta(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();
                if (!response.isEmpty()) {
                    try {
                        DirectionsJSONParser directions = new DirectionsJSONParser();
                        JSONObject respuesta = new JSONObject(response);
                        Log.e("TAG", "onResponse: " + respuesta);
                        List<List<HashMap<String, String>>> routes = directions.parse(respuesta);

                        for (int i = 0; i < routes.size(); i++) {
                            points = new ArrayList();
                            lineOptions = new PolylineOptions();

                            List<HashMap<String, String>> path = routes.get(i);

                            for (int j = 0; j < path.size(); j++) {
                                HashMap<String, String> point = path.get(j);

                                double lat = Double.parseDouble(point.get("lat"));
                                double lng = Double.parseDouble(point.get("lng"));
                                LatLng position = new LatLng(lat, lng);

                                points.add(position);
                            }

                            lineOptions.addAll(points);
                            lineOptions.width(12);
                            lineOptions.color(Color.RED);
                            lineOptions.geodesic(true);

                        }

// Drawing polyline in the Google Map for the i-th route
                        nMap.addPolyline(lineOptions);
                    } catch (Exception e) {
                        Log.e("TAG", "onResponse: " + e);
                    }

                } else {
                    Toast.makeText(MapsActivityTaxista.this, "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivityTaxista.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getDirectionsUrl() {

        // Origin of route
        String str_origin = "origin=" + latitudOrigen + "," + longitudOrigen;

        // Destination of route
        String str_dest = "destination=" + latitudDestino + "," + longitudDestino;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=AIzaSyAnIMIJgWvMTmhp9MzRApGhNaU4h4aBkaE";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

}