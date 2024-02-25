package com.example.quiz.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.quiz.R;
import com.example.quiz.sqlite.DatabaseManager;
import com.example.quiz.sqlite.Person;

import java.util.List;

public class SQLiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();
        // Thêm dữ liệu
        databaseManager.addData("John", 30);
        databaseManager.addData("Alice", 25);
        // Lấy tất cả dữ liệu và hiển thị trong log
        List<Person> dataList = databaseManager.getAllData();
        for (Person person : dataList) {
            Log.d("Person", "ID: " + person.id + ", Name: " + person.name + ", Age: " + person.age);
        }
        databaseManager.close();
    }
}