package com.example.proyectodegrado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class MapsActivityHistorial extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap nMap;
    double latitudOrigen;
    double longitudOrigen;
    double latitudDestino;
    double longitudDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_historial);

        latitudOrigen = getIntent().getExtras().getDouble("latOri");
        longitudOrigen = getIntent().getExtras().getDouble("longOri");
        latitudDestino = getIntent().getExtras().getDouble("latDes");
        longitudDestino = getIntent().getExtras().getDouble("longDes");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapHistorial);
        mapFragment.getMapAsync(this);
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
                    Toast.makeText(MapsActivityHistorial.this, "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivityHistorial.this, error.toString(), Toast.LENGTH_SHORT).show();
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