package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView textViewResult = findViewById(R.id.textViewResult);
        Button buttonBack = findViewById(R.id.buttonBack);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double fahrenheit = extras.getDouble("FAHRENHEIT");
            textViewResult.setText(String.format("Result: %.2f Fahrenheit", fahrenheit));
        }
        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }
}
