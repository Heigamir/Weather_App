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

public class Delete  extends AppCompatActivity {
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        final ArrayList<String> data = ((Variables) this.getApplication()).getData();

        Button btnNavToMain = (Button) findViewById(R.id.btnGoMain);
        btnNavToMain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Delete.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button btnNavToFav = (Button) findViewById(R.id.btnGo);
        btnNavToFav.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Delete.this, Favorites.class);
                startActivity(intent);
            }
        });

        ListView list = (ListView) findViewById(R.id.List);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                data.remove(data.get(position));
                finish();
                startActivity(getIntent());
            }
        });
    }
}
