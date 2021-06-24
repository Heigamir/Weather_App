package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText etCity, etCountry;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        Variables test = ((Variables) this.getApplication());

        Button btnNavToFavorites = (Button) findViewById(R.id.btnGo);
        Button btnAddToFavorites = (Button) findViewById(R.id.btnAdd);

        btnNavToFavorites.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Favorites.class);
                startActivity(intent);
            }
        });

        btnAddToFavorites.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String city=etCity.getText().toString().trim();
                test.setData(city);
                Toast.makeText(MainActivity.this, "Dodano do ulubionych: " + city, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city=etCity.getText().toString().trim();
        String country= etCountry.getText().toString().trim();
        if (city.equals("")){
            tvResult.setText("Pole miasto nie może być pustę!");
        }else {
            if(!country.equals("")){
                tempUrl=Variables.url + "?q=" + city + "," + country + "&lang=pl&appid=" + Variables.appid;
            }else{
                tempUrl=Variables.url + "?q=" + city + "&lang=pl&appid=" + Variables.appid;
            }
            //Aktualna pogoda
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response",response);
                    String output="";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") -273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");

                        tvResult.setTextColor(Color.rgb(255,255,255));

                        output +=" Aktualna Temperatura w " +cityName + " (" + countryName + ")"
                                + "\n Temperatura: " + Variables.df.format(temp) + " °C"
                                + "\n Temperatura odczuwalna: " + Variables.df.format(feelsLike) + " °C"
                                + "\n Wilgotność powietrza: " + humidity + "%"
                                + "\n Opis: " + description
                                + "\n Prędkość wiatru: " + wind + " m/s"
                                + "\n Zachmurzenie: " + clouds + "%"
                                + "\n Ciśnienie: " + pressure + " hPa";
                        tvResult.setText(output);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(),Toast.LENGTH_SHORT).show();
                }
            });


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    public void addToFavorite(View view) {
    }
}