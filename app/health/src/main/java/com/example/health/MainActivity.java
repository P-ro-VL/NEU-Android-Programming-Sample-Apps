package com.example.health;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventCallback sensorListenner;
    private Sensor tempSensor;
    private TextView tempTextView;
    private Sensor pressureSensor;
    private TextView pressureTextView;
    private Sensor humidSensor;
    private TextView humidTextView;
    private Sensor proximitySensor;
    private TextView proximityTextView;
    private Sensor lightSensor;
    private TextView lightTextView;
    private Sensor magneticSensor;
    private TextView magneticTextView;
    private Sensor gyroscopeSensor;
    private TextView stepTextView;
    private Sensor accelerometerSensor;

    private boolean isWalking = false;
    private int stepCount = 0;
    private static final float STEP_THRESHOLD = 12.0f;

    private Button resetButton;
    private ProgressBar progressBar;
    private static final float TARGET = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (tempSensor != null) {
            tempTextView = findViewById(R.id.tempTextView);
            tempTextView.setVisibility(View.VISIBLE);
        }

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressureSensor != null) {
            pressureTextView = findViewById(R.id.pressureTextView);
            pressureTextView.setVisibility(View.VISIBLE);
        }

        humidSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (humidSensor != null) {
            humidTextView = findViewById(R.id.humidTextView);
            humidTextView.setVisibility(View.VISIBLE);
        }

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor != null) {
            proximityTextView = findViewById(R.id.proximityTextView);
            proximityTextView.setVisibility(View.VISIBLE);
        }

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            lightTextView = findViewById(R.id.lightTextView);
            lightTextView.setVisibility(View.VISIBLE);
        }

        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticSensor != null) {
            magneticTextView = findViewById(R.id.magneticTextView);
            magneticTextView.setVisibility(View.VISIBLE);
        }

        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor != null) {
            stepTextView = findViewById(R.id.stepTextView);
            ((ViewGroup)stepTextView.getParent()).setVisibility(View.VISIBLE);
        }

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            stepTextView = findViewById(R.id.stepTextView);
            ((ViewGroup)stepTextView.getParent()).setVisibility(View.VISIBLE);
        }

        sensorListenner = new SensorEventCallback() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    float value = event.values[0];
                    if (value <= 100 && value >= -273)
                        tempTextView.setText("🌡 " + String.format("%.0f", value) + "°C");
                }
                if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                    float value = event.values[0];
                    pressureTextView.setText("🕛 " + String.format("%.0f", value) + "hPA");
                }
                if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    float value = event.values[0];
                    humidTextView.setText("💧 " + String.format("%.0f", value) + "%");
                }
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    float value = event.values[0];
                    proximityTextView.setText("📏 " + String.format("%.0f", value) + "cm");
                }
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    float value = event.values[0];
                    lightTextView.setText("💡 " + String.format("%.0f", value) + "lux");
                }
                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    float[] values = event.values;
                    float azimuth = (float) Math.toDegrees(Math.atan2(values[1], values[0]));
                    if (azimuth < 0) azimuth += 360;
                    magneticTextView.setText("🧭" + String.format("%.0f", azimuth) + "°");
                }
//                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                    float xAxis = event.values[0];
//                    float yAxis = event.values[1];
//                    float zAxis = event.values[2];
//                    stepTextView.setText("X:" + String.format("%.1f", xAxis) + "\nY: " + String.format("%.1f", yAxis) + "\nZ: " + String.format("%.1f", zAxis));
//                }
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    // Tính toán gia tốc
                    float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

                    // Kiểm tra nếu đang đi bộ và có sự thay đổi trong gia tốc độ dọc
                    if (isWalking && acceleration < STEP_THRESHOLD) {
                        isWalking = false;
                    } else if (!isWalking && acceleration > STEP_THRESHOLD) {
                        isWalking = true;
                        stepCount++;
                        stepTextView.setText("" + stepCount);
                        progressBar.setProgress((int) ((100 * stepCount) / TARGET));
                    }
                }
            }
        };
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);
        resetButton.setOnClickListener(v -> {
            stepCount = 0;
            stepTextView.setText("0");
            progressBar.setProgress(0);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(sensorListenner, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, humidSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListenner);
    }
}