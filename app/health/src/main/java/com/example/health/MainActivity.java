package com.example.health;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tempTextView;
    private TextView pressureTextView;
    private TextView humidTextView;
    private TextView proximityTextView;
    private TextView lightTextView;
    private TextView magneticTextView;
    private TextView stepTextView;
    private Button resetButton;
    private SensorEventListener sensorListenner;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private Sensor humidSensor;
    private Sensor pressureSensor;
    private Sensor proximitySensor;
    private Sensor lightSensor;
    private Sensor magneticSensor;
    private Sensor stepSensor;
    private boolean isWalking = false;
    private int stepCount = 0;
    private static final float STEP_THRESHOLD = 12.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempTextView = findViewById(R.id.tempTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        humidTextView = findViewById(R.id.humidTextView);
        proximityTextView = findViewById(R.id.proximityTextView);
        lightTextView = findViewById(R.id.lightTextView);
        magneticTextView = findViewById(R.id.magneticTextView);
        stepTextView = findViewById(R.id.stepTextView);
        resetButton = findViewById(R.id.resetButton);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Ambient Temperature
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null)
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        //Relative Humidity
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null)
            humidSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        //Pressure
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null)
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        //Proximity
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Light
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //Magnetic Field
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null)
            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //        //Gyroscope
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
//            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        }

        //Accelerometer
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        sensorListenner = new SensorEventCallback() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    float value = event.values[0];
                    if (value <= 100 && value >= -273)
                        tempTextView.setText("\uD83C\uDF21 \n\n" + String.format("%.0f", value) + "°C");
                    else tempTextView.setText("\uD83C\uDF21 \n\n" + "0 °C");
                }
                if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                    float value = event.values[0];
                    pressureTextView.setText("\uD83D\uDD5B \n\n" + String.format("%.0f", value) + " hPA");
                }
                if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    float value = event.values[0];
                    humidTextView.setText("\uD83D\uDCA7 \n\n" + String.format("%.0f", value) + " %");
                }

                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    float value = event.values[0];
                    proximityTextView.setText("\uD83D\uDCCF \n\n" + String.format("%.0f", value) + " ");
                }

                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    float value = event.values[0];
                    lightTextView.setText("\uD83D\uDCA1 \n\n" + String.format("%.0f", value) + " lux");
                }

                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    float[] values = event.values;
                    float azimuth = (float) Math.toDegrees(Math.atan2(values[1], values[0]));
                    if (azimuth < 0) azimuth += 360;
                    magneticTextView.setText("\uD83E\uDDED \n\n" + String.format("%.0f", azimuth) + "°");
                }

                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    float xAxis = event.values[0];
                    float yAxis = event.values[1];
                    float zAxis = event.values[2];
                    stepTextView.setText("X: " + xAxis + " rad/s\nY: " + yAxis + " rad/s\nZ: " + zAxis + "  rad/s");
                }

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
//                    stepTextView.setText("X: " + xAxis + " m/s²\nY: " + yAxis + " m/s²\nZ: " + zAxis + " m/s²");

                    // Tính toán gia tốc dọc
                    float verticalAcceleration = (float) Math.sqrt(x * x + y * y);

                    // Kiểm tra nếu đang đi bộ và có sự thay đổi trong gia tốc độ dọc
                    if (isWalking && verticalAcceleration < STEP_THRESHOLD) {
                        isWalking = false;
                    } else if (!isWalking && verticalAcceleration > STEP_THRESHOLD) {
                        isWalking = true;
                        stepCount++;
                        stepTextView.setText("" + stepCount);
                    }
                }
            }
        };

        sensorManager.registerListener(sensorListenner, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, humidSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListenner, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);

        resetButton.setOnClickListener(v -> {
            stepCount=0;
            stepTextView.setText("0");
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorListenner);
    }
}