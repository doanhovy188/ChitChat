package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chitchat.R;
import com.example.chitchat.adapter.StoryAdapter;
import com.example.chitchat.adapter.UsersAdapter;
import com.example.chitchat.databinding.ActivityMainBinding;
import com.example.chitchat.models.Story;
import com.example.chitchat.models.User;
import com.example.chitchat.models.UserStory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;
    ArrayList<UserStory> stories;
    User currentUser;
    UsersAdapter usersAdapter;
    StoryAdapter storyAdapter;

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

        stories = new ArrayList<>();

        storyAdapter = new StoryAdapter(this, stories);

        binding.StoriesRecycleView.setAdapter(storyAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        binding.StoriesRecycleView.setLayoutManager(linearLayoutManager);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);


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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("story").addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                        UserStory userStories = new UserStory();
                        userStories.setName(storySnapshot.child("name").getValue(String.class));
                        userStories.setAvatar(storySnapshot.child("avatar").getValue(String.class));
                        userStories.setLastUpdated(storySnapshot.child("lastUpdate").getValue(long.class));
                        ArrayList<Story> storiesTemp = new ArrayList<>();
                        for (DataSnapshot story : storySnapshot.child("stories").getChildren()) {
                            Story st = story.getValue(Story.class);
                            storiesTemp.add(st);
                        }
                        userStories.setStories(storiesTemp);
                        stories.add(userStories);
                    }
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.createStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 75);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://chitchat-3f357.appspot.com");
                Date date = new Date();
                StorageReference reference = storage.getReference().child("story").child(date.getTime() + "");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStory userStory = new UserStory();
                                    userStory.setAvatar(currentUser.getAvatar());
                                    userStory.setName(currentUser.getName());
                                    userStory.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStory.getName());
                                    obj.put("avatar", userStory.getAvatar());
                                    obj.put("lastUpdate", userStory.getLastUpdated());

                                    String imgUrl = uri.toString();
                                    Story story = new Story(imgUrl, userStory.getLastUpdated());

                                    database.getReference()
                                            .child("story")
                                            .child(currentUser.getUserID())
                                            .updateChildren(obj);
                                    database.getReference()
                                            .child("story")
                                            .child(currentUser.getUserID())
                                            .child("stories")
                                            .push()
                                            .setValue(story);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("Login_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                prefEditor.remove("phone");
                prefEditor.remove("password");
                prefEditor.remove("Login_data");
                prefEditor.apply();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
//                Toast.makeText(this, "group clicked.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, GroupChatActivity.class));
                break;
            case R.id.search:
                Toast.makeText(this, "Search clicked.", Toast.LENGTH_SHORT).show();
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