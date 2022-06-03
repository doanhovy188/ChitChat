package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chitchat.R;
import com.example.chitchat.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnStart extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_start);

        SharedPreferences sharedPreferences = getSharedPreferences("Login_data", Context.MODE_PRIVATE);


        if (sharedPreferences.contains("phone")) {
            String phone = sharedPreferences.getString("phone", "defaultStringIfNothingFound");
            String password = sharedPreferences.getString("password", "defaultStringIfNothingFound");

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app");

            database.getReference().child("users").child(phone).addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user == null) {
                        Toast.makeText(OnStart.this, "Phone Number not found!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.equals(user.getPassword())) {
                            Intent intent = new Intent(OnStart.this, MainActivity.class);
                            intent.putExtra("userID", user.getUserID());
                            startActivity(intent);
                            Toast.makeText(OnStart.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OnStart.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Intent intent = new Intent(OnStart.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}