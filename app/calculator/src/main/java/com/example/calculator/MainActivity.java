package com.example.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {
    private TextView editTextText;
    private TextView equationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextText = findViewById(R.id.textViewResult);
        equationTextView = findViewById(R.id.equationTextView);

        loadRecentResult();

        setButtonClickListeners();
        handleTheme();
    }

    public static final String RECENT_RESULT_KEY = "recent_result";

    private void saveRecentResult(String result) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(RECENT_RESULT_KEY, result);
        editor.apply();
    }

    private void loadRecentResult() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        String recentValue = sharedPref.getString(RECENT_RESULT_KEY, defaultValue);

        editTextText.setText(recentValue);
    }

    private boolean isNightMode = false;
    private void handleTheme() {
        Button btn = findViewById(R.id.themeModeBtn);
        btn.setOnClickListener((e) -> {
            isNightMode = !isNightMode;
            AppCompatDelegate.setDefaultNightMode(
                    isNightMode ?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void setButtonClickListeners() {
        int[] buttonIds = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonDot, R.id.buttonAC, R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonPercent, R.id.buttonParentheses, R.id.buttonEqual, R.id.buttonBack};
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(view -> onButtonClick(view));
        }
    }

    private void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        if(!buttonText.equals("=")) {
            equationTextView.setText("");
        }

        switch (buttonText) {
            case "=":
                if(button.isEnabled()) {
                    calculateResult(true);
                    saveRecentResult(editTextText.getText().toString());
                }
                else return;
                break;
            case "()":
                handleParentheses();
                break;
            case "⌫":
                removeLastInput();
                break;
            case "AC":
                clearInput();
                break;
            default:
                appendInput(buttonText);
                break;
        }
    }

    private void appendInput(String input) {
        Button equalBtn = findViewById(R.id.buttonEqual);

        editTextText.setText(editTextText.getText().toString() + input);

        boolean result = calculateResult(false);
        equalBtn.setEnabled(result);
        GradientDrawable background = new GradientDrawable();
        background.setCornerRadius(Integer.MAX_VALUE * getResources().getDisplayMetrics().density);
        if(!result) {
            background.setTint(getBaseContext().getColor(R.color.smoke));
        } else {
            background.setTint(getBaseContext().getColor(R.color.light_orange));
        }
        equalBtn.setBackground(background);
    }

    private void removeLastInput() {
        String s = editTextText.getText().toString();
        if (s.length() > 0) {
            editTextText.setText(s.substring(0, s.length() - 1));
        }
    }

    private void clearInput() {
        editTextText.setText("");
    }

    private boolean isOpenParentheses = false;
    private void handleParentheses() {
        if (isOpenParentheses) {
            appendInput(")");
            isOpenParentheses = false;
        } else {
            appendInput("(");
            isOpenParentheses = true;
        }
    }

    private boolean calculateResult(boolean showResult) {
        try {
            String expression = editTextText.getText().toString();
            Expression expressionEval = new Expression(expression);
            double result = expressionEval.calculate();

            if(Double.isNaN(result)) return false;
            if(showResult) {
                equationTextView.setText(editTextText.getText());
                editTextText.setText(String.valueOf(result));
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
