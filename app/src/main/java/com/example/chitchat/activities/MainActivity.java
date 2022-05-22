package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chitchat.R;
import com.example.chitchat.adapter.UsersAdapter;
import com.example.chitchat.databinding.ActivityMainBinding;
import com.example.chitchat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;
    User currentUser;
    UsersAdapter usersAdapter;
    //    TopStatusAdapter statusAdapter;
//    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String userID = getIntent().getStringExtra("userID");

        database = FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app");

        users = new ArrayList<>();

        usersAdapter = new UsersAdapter(this, users, userID);

        binding.UsersRecycleView.setAdapter(usersAdapter);

        binding.UsersRecycleView.setLayoutManager(new LinearLayoutManager(this));

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
//                    System.out.println(FirebaseAuth.getInstance().getUid());
                    if (!user.getUserID().equals(userID))
                        users.add(user);
                    else currentUser = user;
                }
                usersAdapter.notifyDataSetChanged();

                Picasso.get().load(currentUser.getAvatar()).into(binding.currentUserAvatar);

                binding.Welcome.setText("Welcome back\n" + currentUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group:
                Toast.makeText(this, "group clicked.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, GroupChatActivity.class));
                break;
            case R.id.search:
                Toast.makeText(this, "Search clicked.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings Clicked.", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}