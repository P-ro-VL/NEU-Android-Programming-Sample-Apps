package com.example.quiz.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm một dòng mới vào cơ sở dữ liệu
    public void addData(String name, int age) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_AGE, age);
        database.insert(DBHelper.TABLE_NAME, null, values);
    }

    // Lấy tất cả dữ liệu từ cơ sở dữ liệu
    public List<Person> getAllData() {
        List<Person> dataList = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_AGE));
            dataList.add(new Person(id, name, age));
            cursor.moveToNext();
        }
        cursor.close();
        return dataList;
    }
}
