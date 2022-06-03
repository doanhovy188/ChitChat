package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chitchat.databinding.ActivitySetupProfileBinding;
import com.example.chitchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class SetupProfileActivity extends AppCompatActivity {

    //    ActivitySetupProfileBinding binding;
    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImageUri;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hide ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //set dialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance();

        binding.avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);
            }
        });

        binding.setupProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                String name = binding.inputName.getText().toString();

                if (name.isEmpty()) {
                    binding.inputName.setError("Please type a name!");
                    return;
                }

                if (selectedImageUri != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uid = auth.getUid();
                                        String avatarUrl = uri.toString();
                                        String name = binding.inputName.getText().toString();
                                        User user = new User(uid, name, Login.PHONENUMBER, Login.PASSWORD, avatarUrl);
                                        Log.e("log", user.toString());
                                        database.getReference()
                                                .child("users")
                                                .child(Login.PHONENUMBER)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(SetupProfileActivity.this, Login.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                } else {
                    String uid = auth.getUid();
                    String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/chitchat-3f357.appspot.com/o/Profiles%2Favatar.png?alt=media&token=59085f17-ff6e-4bdb-ace7-13cce345a5fd";
                    User user = new User(uid, name, Login.PHONENUMBER, Login.PASSWORD, avatarUrl);
                    Log.e("log", user.toString());
                    database.getReference()
                            .child("users")
                            .child(Login.PHONENUMBER)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SetupProfileActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                binding.avatarImageView.setImageURI(data.getData());
                selectedImageUri = data.getData();
            }
        }
    }


}