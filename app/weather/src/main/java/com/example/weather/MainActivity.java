package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private TextView textView;
    private TextView locationTextView;
    private TextView tempTextView;
    private TextView humidTextView;
    private TextView cloudTextView;
    private TextView rainTextView;
    private TextView windTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        textView = findViewById(R.id.textView);
        locationTextView = findViewById(R.id.locationTextView);
        tempTextView = findViewById(R.id.tempTextView);
        humidTextView = findViewById(R.id.humidTextView);
        cloudTextView = findViewById(R.id.cloudTextView);
        rainTextView = findViewById(R.id.rainTextView);
        windTextView = findViewById(R.id.windTextView);
        progressBar = findViewById(R.id.progressBar);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(googleMap -> {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12));
                        googleMap.addMarker(new MarkerOptions().position(myLocation).title("You are here"));
                        showInfo(myLocation);
                    }
                });
                googleMap.setOnCameraIdleListener(() -> {
                    LatLng myLocation = googleMap.getCameraPosition().target;
                    showInfo(myLocation);
                });
            });
        }
    }

    private void showInfo(LatLng location) {
        textView.setText(convertToDegreeMinutesSeconds(location.longitude) + ", " + convertToDegreeMinutesSeconds(location.latitude));
        locationTextView.setText(getCityName(location.latitude, location.longitude));


        progressBar.setVisibility(View.VISIBLE);
        tempTextView.setVisibility(View.INVISIBLE);
        humidTextView.setVisibility(View.INVISIBLE);
        cloudTextView.setVisibility(View.INVISIBLE);
        rainTextView.setVisibility(View.INVISIBLE);
        windTextView.setVisibility(View.INVISIBLE);
        new Thread(() -> {
            try {
                String API_URL = "https://api.open-meteo.com/v1/forecast?latitude=" + location.latitude + "&longitude=" + location.longitude + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,rain,showers,snowfall,is_day,cloud_cover,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m";
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                JSONObject baseJsonResponse = new JSONObject(response.toString());
                JSONObject currentJsonObject = baseJsonResponse.getJSONObject("current");
                double temperature_2m = currentJsonObject.getDouble("temperature_2m");
                double relative_humidity_2m = currentJsonObject.getDouble("relative_humidity_2m");
                double rain = currentJsonObject.getDouble("rain");
                double cloud_cover = currentJsonObject.getDouble("cloud_cover");
                double wind_speed_10m = currentJsonObject.getDouble("wind_speed_10m");
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tempTextView.setVisibility(View.VISIBLE);
                    humidTextView.setVisibility(View.VISIBLE);
                    cloudTextView.setVisibility(View.VISIBLE);
                    rainTextView.setVisibility(View.VISIBLE);
                    windTextView.setVisibility(View.VISIBLE);
                    tempTextView.setText("🌡️" + (int) temperature_2m + "°C");
                    humidTextView.setText("💧" + (int) relative_humidity_2m + "%");
                    rainTextView.setText("☁\n" + (int) cloud_cover + "%");
                    cloudTextView.setText("🌧\n" + (int) rain + "mm");
                    windTextView.setText("🌪\n" + (int) wind_speed_10m + "km/h");
                });
                urlConnection.disconnect();
            } catch (Exception e) {


            }
        }).start();
    }

    public static String convertToDegreeMinutesSeconds(double coordinate) {
        int degree = (int) coordinate;
        coordinate = (coordinate - degree) * 60;
        int minute = (int) coordinate;
        double second = (coordinate - minute) * 60;
        return degree + "° " + minute + "' " + (int) second + "\"";
    }


    private String getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAdminArea();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "-";
        }
        return "-";
    }

}