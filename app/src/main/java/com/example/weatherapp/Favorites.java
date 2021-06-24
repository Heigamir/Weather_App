package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Favorites extends AppCompatActivity {
    private ArrayAdapter adapter;
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        final ArrayList<String> data = ((Variables) this.getApplication()).getData();

        tvResult = findViewById(R.id.tv);

        Button btnNavToMain = (Button) findViewById(R.id.btnGoMain);
        btnNavToMain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorites.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnNavToDel = (Button) findViewById(R.id.btnGoDelete);
        btnNavToDel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorites.this, Delete.class);
                startActivity(intent);
            }
        });

        ListView list = (ListView) findViewById(R.id.List);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tempUrl = "";
                String city=data.get(position);
                tempUrl=Variables.url + "?q=" + city + "&lang=pl&appid=" + Variables.appid;

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
        });
    }
}
