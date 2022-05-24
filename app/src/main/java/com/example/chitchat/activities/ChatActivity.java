package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.R;
import com.example.chitchat.adapter.MessagesAdapter;
import com.example.chitchat.databinding.ActivityChatBinding;
import com.example.chitchat.models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    MessagesAdapter adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;
    String senderID;
    String receiverID;
    String token;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        String userName = getIntent().getStringExtra("name");
        String receiverAvatar = getIntent().getStringExtra("avatar");

        receiverID = getIntent().getStringExtra("receiverID");

        senderID = getIntent().getStringExtra("senderID");

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

        binding.name.setText(userName);
        Picasso.get().load(receiverAvatar).into(binding.profile);

        messages = new ArrayList<>();

        MessagesAdapter messagesAdapter = new MessagesAdapter(this, messages, senderID, receiverID);
        binding.MessageListview.setAdapter(messagesAdapter);

        database = FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app");

        database.getReference().child("chatRooms").child(senderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int countOldMsg = messages.size();
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messages.add(message);
                        }
                        messagesAdapter.notifyDataSetChanged();
                        if (messages.size() > countOldMsg)
                            scrollMyListViewToBottom();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.backBtn.setOnClickListener(view1 -> {
            finish();
        });

        binding.sendBtn.setOnClickListener(view1 -> {
            String One_message = binding.messageBox.getText().toString();

            Date date = new Date();
            Message message = new Message(One_message, senderID, date.getTime());

            database.getReference().child("chatRooms").child(senderRoom).child("messages").push().setValue(message)
                    .addOnSuccessListener(unused -> {
                        database.getReference().child("chatRooms").child(receiverRoom).child("messages").push().setValue(message)
                                .addOnSuccessListener(unused1 -> {

                                });
                        HashMap<String, Object> lastMsgObj = new HashMap<>();
                        lastMsgObj.put("lastMsg", message.getMessage());
                        lastMsgObj.put("lastMsgTime",date.getTime());
                        lastMsgObj.put("senderId", message.getSenderId());

                        database.getReference().child("chatRooms").child(senderRoom).updateChildren(lastMsgObj);
                        database.getReference().child("chatRooms").child(receiverRoom).updateChildren(lastMsgObj);
                    });
            binding.messageBox.setText("");
        });
    }

    private void scrollMyListViewToBottom() {
        ListView listView = findViewById(R.id.MessageListview);
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(messages.size() - 1);
            }
        });
    }
}