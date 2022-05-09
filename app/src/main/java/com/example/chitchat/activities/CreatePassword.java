package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivityCreatePasswordBinding;
import com.example.chitchat.databinding.ActivitySetupProfileBinding;

public class CreatePassword extends AppCompatActivity {

    ActivityCreatePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String phoneNumber = getIntent().getStringExtra("phoneNumber2");

        binding.CreatePassBtn.setOnClickListener(view -> {
            String pass = binding.PassWord.getText().toString();
            String re_pass = binding.rePassWord.getText().toString();

            if (!pass.equals(re_pass)) {

                Toast.makeText(CreatePassword.this, "Password doesn't match!!!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CreatePassword.this, SetupProfileActivity.class);
                Login.PASSWORD=binding.PassWord.getText().toString();
                startActivity(intent);
                finish();
            }
        });
    }
}