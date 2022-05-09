package com.example.chitchat.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.activities.ChatActivity;
import com.example.chitchat.models.User;
import com.example.chitchat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends BaseAdapter {

    AppCompatActivity appCompatActivity;
    ArrayList<User> users;

    String user_loggedIn_ID;

    public UsersAdapter(AppCompatActivity appCompatActivity, ArrayList<User> users, String user_loggedIn_ID) {
        this.appCompatActivity = appCompatActivity;
        this.users = users;
        this.user_loggedIn_ID = user_loggedIn_ID;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        view = inflater.inflate(R.layout.row_conversation, null);

        TextView userName = view.findViewById(R.id.username);
        userName.setText(users.get(i).getName());

        CircleImageView circleImageView = view.findViewById(R.id.profile);

        Picasso.get().load(users.get(i).getAvatar()).into(circleImageView);

        TextView time=view.findViewById(R.id.tiime);

        view.setOnClickListener(view1 -> {
            Intent intent = new Intent(this.appCompatActivity, ChatActivity.class);
            intent.putExtra("name", users.get(i).getName());
            intent.putExtra("avatar", users.get(i).getAvatar());
            intent.putExtra("senderID", user_loggedIn_ID);
            intent.putExtra("receiverID", users.get(i).getUserID());
//            intent.putExtra("token", users.get(i).getToken());
            this.appCompatActivity.startActivity(intent);
        });

        return view;
    }
}
