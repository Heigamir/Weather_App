package com.example.weatherapp;

import android.app.Application;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Variables extends Application {
    public static String url ="http://api.openweathermap.org/data/2.5/weather";
    public static String appid ="722071a67f8e95f7afe8c28b3f625652";
    public static DecimalFormat df = new DecimalFormat("#.##");

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(String data) {
        this.data.add(data);
    }

    public final ArrayList<String> data = new ArrayList<>();
}
