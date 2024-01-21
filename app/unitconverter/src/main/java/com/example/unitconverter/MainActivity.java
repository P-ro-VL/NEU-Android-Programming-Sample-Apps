package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextCelsius = findViewById(R.id.editTextCelsius);
        Button buttonConvert = findViewById(R.id.buttonConvert);

        buttonConvert.setOnClickListener(v -> {
            double celsius = Double.parseDouble(editTextCelsius.getText().toString());
            double fahrenheit = (celsius * 9/5) + 32;

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("FAHRENHEIT", fahrenheit);
            startActivity(intent);
        });
    }
}