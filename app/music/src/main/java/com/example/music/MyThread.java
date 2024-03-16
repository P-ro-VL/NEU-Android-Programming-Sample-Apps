package com.example.music;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyThread extends Thread {
    String imageUrl;
    Context context;

    public MyThread(String imageUrl, Context context) {
        this.imageUrl = imageUrl;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(imageUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            ((Activity)context).runOnUiThread(() -> {
                ((MainActivity)context).imageView.setImageBitmap(bitmap);
            });
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

