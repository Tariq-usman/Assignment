package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    Dialog dialog;
    String key = "&appid=4887a6302ae9554b004a353eed8a59d7";
    PreferencesClass preferencesClass;
    String location;
    private TextView tv_city,tv_humidity,tv_pressure,tv_temp;
    FirebaseAuth auth;
    Double a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        dialog = Utils.dialog(HomeActivity.this);
        preferencesClass = new PreferencesClass(getApplicationContext());
        location = preferencesClass.getUserLocation();
        tv_city = findViewById(R.id.city_field);
        tv_humidity = findViewById(R.id.humidity_field);
        tv_pressure = findViewById(R.id.pressure_field);
        tv_temp = findViewById(R.id.current_temperature_field);


        getWeatherDetails();

        findViewById(R.id.btn_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(HomeActivity.this,SignInActivity.class));
                finish();
            }
        });
    }

    private void getWeatherDetails() {
        dialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Gson gson = new GsonBuilder().create();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.base_url + location + key, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PojoResponse pojoResponse = gson.fromJson(response,PojoResponse.class);
                    tv_city.setText(pojoResponse.getName());
                    tv_humidity.setText("Humidity: "+pojoResponse.getMain().getHumidity().toString());
                    tv_pressure.setText("Pressure: "+pojoResponse.getMain().getPressure().toString());
                    a=Double.parseDouble(String.valueOf(pojoResponse.getMain().getTemp()));
                    Double b=a-32;
                    Double c=b*5/9;
                    String temp = String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(c)));
                    tv_temp.setText("Temperature: "+temp +"°C");
                    dialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    dialog.dismiss();
                }
//                Toast.makeText(HomeActivity.this, "Response", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}