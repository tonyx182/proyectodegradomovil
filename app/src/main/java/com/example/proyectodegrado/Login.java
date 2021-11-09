package com.example.proyectodegrado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText usuario, password;
    Button btnIngresar;
    String user, contrasenia;
    ResponseLogin data;
    public int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario=findViewById(R.id.txtEmail);
        password=findViewById(R.id.txtPassword);
        btnIngresar=findViewById(R.id.btnIngresar);

        recuperarPreferencias();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = usuario.getText().toString();
                contrasenia = password.getText().toString();
                if (!user.isEmpty() && !contrasenia.isEmpty()){
                    validarLogin("http://192.168.1.5/codeigniter/proyectodegrado/index.php/restserver/login");
                }else {
                    Toast.makeText(Login.this, "Debe Ingresar sus Datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarLogin(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Gson datos = new Gson();
                    data = datos.fromJson(response, ResponseLogin.class);
                    idUsuario = data.data.idUsuario;
                    Log.e("TAG", "onResponse:" + data.data.idRol);
                    if (data.data.idRol == 2) {
                        guardarPreferencias();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (data.data.idRol == 3) {
                        guardarPreferencias();
                        Intent intent = new Intent(getApplicationContext(),MainActivityTaxista.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(Login.this, "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Login.this, "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("userName",user);
                parametros.put("password",contrasenia);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private  void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",user);
        editor.putString("password",contrasenia);
        editor.putInt("idUsuario",data.data.idUsuario);
        editor.putBoolean("sesion", true);
        editor.commit();
    }



    private  void recuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        usuario.setText(preferences.getString("userName", ""));
        password.setText(preferences.getString("password", ""));
    }

}