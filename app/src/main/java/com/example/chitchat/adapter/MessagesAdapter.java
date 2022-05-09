package com.example.chitchat.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.models.Message;
import com.example.chitchat.models.User;
import com.google.firebase.auth.FirebaseAuth;

import com.example.chitchat.R;

import java.util.ArrayList;
import java.util.Date;

public class MessagesAdapter extends BaseAdapter {

    AppCompatActivity appCompatActivity;
    ArrayList<Message> messages;

    String user_loggedIn_ID;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public MessagesAdapter(AppCompatActivity appCompatActivity, ArrayList<Message> messages, String user_loggedIn_ID) {
        this.appCompatActivity = appCompatActivity;
        this.messages = messages;
        this.user_loggedIn_ID = user_loggedIn_ID;
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (user_loggedIn_ID.equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        TextView one_message;
        TextView time;
        if (getItemViewType(i) == ITEM_RECEIVE) {
            view = inflater.inflate(R.layout.item_receive, null);
            one_message = view.findViewById(R.id.receive_message);
            one_message.setText(messages.get(i).getMessage());

            time = view.findViewById(R.id.message_receive_time);
            time.setText(String.valueOf(new Date(messages.get(i).getTimesent())));
        } else {
            view = inflater.inflate(R.layout.item_sent, null);
            one_message = view.findViewById(R.id.sent_message);
            one_message.setText(messages.get(i).getMessage());

            time = view.findViewById(R.id.message_sent_time);
            time.setText(String.valueOf(new Date(messages.get(i).getTimesent())));
        }
        return view;
    }
}
