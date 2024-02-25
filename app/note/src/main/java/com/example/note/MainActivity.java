package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CalendarView calendarView;
    EditText noteEditText;
    Button saveButton;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendarView);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int savedYear = pref.getInt("year", 0);
        if(savedYear!=0){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,savedYear);
            cal.set(Calendar.MONTH,pref.getInt("month", 0));
            cal.set(Calendar.DAY_OF_MONTH,pref.getInt("dayOfMonth", 0));
            calendarView.setDate(cal.getTimeInMillis());
        }
        // Xử lý sự kiện khi ngày trên CalendarView được chọn
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            fileName = String.format("%02d_%02d_%04d", dayOfMonth, month + 1, year);
            noteEditText.setText("");
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putInt("year", year).apply();
            editor.putInt("month", month).apply();
            editor.putInt("dayOfMonth", dayOfMonth).apply();
            try {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                fis.close();
                noteEditText.setText(sb);


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        saveButton.setOnClickListener(view -> {
            String noteContent = noteEditText.getText().toString();
            try {
                // Ghi nội dung vào file
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                fos.write(noteContent.getBytes());
                fos.close();
                Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi lưu ghi chú", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("12");
        return super.onCreateOptionsMenu(menu);
    }
}