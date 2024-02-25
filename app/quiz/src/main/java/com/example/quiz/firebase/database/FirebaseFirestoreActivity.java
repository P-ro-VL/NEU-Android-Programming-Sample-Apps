package com.example.quiz.firebase.database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirebaseFirestoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_firestore);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tên_bảng").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Xử lý dữ liệu từ mỗi document
                    }
                } else {
                    Log.d("debug", "Lỗi khi lấy dữ liệu: ", task.getException());
                }
            }
        });

        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "value2");

        db.collection("tên_bảng").document("tên_tài_liệu").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("debug", "Dữ liệu đã được ghi thành công!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("debug", "Lỗi khi ghi dữ liệu", e);
            }
        });


    }
}