package com.example.quiz;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Question.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase db;


    public abstract QuestionDao questionDao();

    public static AppDatabase getDatabase(final Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "quiz_database").addCallback(new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        List<Question> questions = loadQuestionsFromAsset(context);
                        getDatabase(context).questionDao().insertAll(questions);
                    });
                }
            }).build();
        }
        return db;
    }


    public static List<Question> loadQuestionsFromAsset(Context context) {
        try {
            InputStream is = context.getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Question>>() {
            }.getType();
            return gson.fromJson(json, listType);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}

