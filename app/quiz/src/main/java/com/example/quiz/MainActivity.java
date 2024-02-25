package com.example.quiz;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private Button backButton, nextButton;
    private int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionTextView = findViewById(R.id.questionTextView);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        optionButton4 = findViewById(R.id.optionButton4);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        new Thread(() -> {
            questions.addAll(db.questionDao().getAll());
            runOnUiThread(() -> showQuestion(questions.get(0)));
        }).start();

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            currentIndex = currentIndex + 1;
            showQuestion(questions.get(currentIndex));


        });
        backButton.setOnClickListener(v -> {
            currentIndex = currentIndex -  1;
            showQuestion(questions.get(currentIndex));
        });

        View.OnClickListener optionListener = view -> {
            Button b = (Button) view;
            String answer = b.getText().toString();
            if (answer.equals(questions.get(currentIndex).answer)) {
                score++;
            }
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion(questions.get(currentIndex));
            } else {
                questionTextView.setText("Quiz Finished! Your score: " + score);
                optionButton1.setVisibility(View.GONE);
                optionButton2.setVisibility(View.GONE);
                optionButton3.setVisibility(View.GONE);
                optionButton4.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        };
        optionButton1.setOnClickListener(optionListener);
        optionButton2.setOnClickListener(optionListener);
        optionButton3.setOnClickListener(optionListener);
        optionButton4.setOnClickListener(optionListener);

    }

    private void showQuestion(Question question) {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> options = new Gson().fromJson(question.options, type);
        optionButton1.setVisibility(View.VISIBLE);
        optionButton2.setVisibility(View.VISIBLE);
        optionButton3.setVisibility(View.VISIBLE);
        optionButton4.setVisibility(View.VISIBLE);

        questionTextView.setText(question.content);
        optionButton1.setText(options.get(0));
        optionButton2.setText(options.get(1));
        optionButton3.setText(options.get(2));
        optionButton4.setText(options.get(3));

        if (currentIndex > 0) {
            backButton.setVisibility(View.VISIBLE);
        }else{
            backButton.setVisibility(View.GONE);
        }

    }

}