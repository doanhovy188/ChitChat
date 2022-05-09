package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chitchat.databinding.ActivityVerifyBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class VerifyActivity extends AppCompatActivity {

    ActivityVerifyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.inputPhoneNumber.requestFocus();

        binding.continueVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifyActivity.this, OTPActivity.class);
                Login.PHONENUMBER = binding.inputPhoneNumber.getText().toString();
                startActivity(intent);
                finish();
            }
        });
    }
}