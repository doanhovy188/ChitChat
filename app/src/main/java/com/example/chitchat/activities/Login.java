package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivityLoginBinding;
import com.example.chitchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;

    FirebaseDatabase database;

    static String PHONENUMBER;
    static String PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.LoginBtn.setOnClickListener(view -> {
            String phone = binding.LoginPhoneNum.getText().toString();
            String password = binding.LoginPassWord.getText().toString();

            database = FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app");

            database.getReference().child("users").child(phone).addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user == null) {
                        Toast.makeText(Login.this, "Phone Number not found!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (password.equals(user.getPassword())) {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("userID", user.getUserID());
                            startActivity(intent);
                            Toast.makeText(Login.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        binding.RegisterBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, VerifyActivity.class);
            startActivity(intent);
            finish();
        });
    }
}