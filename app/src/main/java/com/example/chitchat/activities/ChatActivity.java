package com.example.chitchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.R;
import com.example.chitchat.adapter.MessagesAdapter;
import com.example.chitchat.databinding.ActivityChatBinding;
import com.example.chitchat.models.Message;
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

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
        storage = FirebaseStorage.getInstance("gs://chitchat-3f357.appspot.com");

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

        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });



        binding.sendBtn.setOnClickListener(view1 -> {
            String One_message = binding.messageBox.getText().toString();

            Date date = new Date();
            Message message = new Message(One_message, senderID, date.getTime());
            HashMap<String, Object> tempLastMsgObj = new HashMap<>();
            tempLastMsgObj.put("lastMsg", "");
            tempLastMsgObj.put("lastMsgTime",0);
            tempLastMsgObj.put("senderId", "");

            database.getReference().child("chatRooms").child(senderRoom).updateChildren(tempLastMsgObj);
            database.getReference().child("chatRooms").child(receiverRoom).updateChildren(tempLastMsgObj);
            String randomKey = database.getReference().push().getKey();
            database.getReference().child("chatRooms").child(senderRoom).child("messages").child(randomKey).setValue(message)
                    .addOnSuccessListener(unused -> {
                        database.getReference().child("chatRooms").child(receiverRoom).child("messages").child(randomKey).setValue(message)
                                .addOnSuccessListener(unused1 -> {
                                    database.getReference().child("chatRooms").child(senderRoom).updateChildren(lastMsgObj)
                                            .addOnSuccessListener(unused2 -> {
                                                System.out.println("unused2");
                                                database.getReference().child("chatRooms").child(receiverRoom).updateChildren(lastMsgObj);
                                            });
                                });
                    });
            binding.messageBox.setText("");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 25){
            if (data != null){
                if (data.getData() != null){
                    dialog.show();
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("Chats").child(calendar.getTimeInMillis() + "");
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();
                                        Date date = new Date();
                                        Message message = new Message("Image", senderID, date.getTime(), filePath);
                                        HashMap<String, Object> tempLastMsgObj = new HashMap<>();
                                        tempLastMsgObj.put("lastMsg", "");
                                        tempLastMsgObj.put("lastMsgTime",0);
                                        tempLastMsgObj.put("senderId", "");

                                        database.getReference().child("chatRooms").child(senderRoom).updateChildren(tempLastMsgObj);
                                        database.getReference().child("chatRooms").child(receiverRoom).updateChildren(tempLastMsgObj);
                                        String randomKey = database.getReference().push().getKey();
                                        database.getReference().child("chatRooms").child(senderRoom).child("messages").child(randomKey).setValue(message)
                                                .addOnSuccessListener(unused -> {
                                                    database.getReference().child("chatRooms").child(receiverRoom).child("messages").child(randomKey).setValue(message)
                                                            .addOnSuccessListener(unused1 -> {
                                                            });
                                                    HashMap<String, Object> lastMsgObj = new HashMap<>();
                                                    lastMsgObj.put("lastMsg", message.getMessage());
                                                    lastMsgObj.put("lastMsgTime",date.getTime());
                                                    lastMsgObj.put("senderId", message.getSenderId());

                                                    database.getReference().child("chatRooms").child(senderRoom).updateChildren(lastMsgObj);
                                                    database.getReference().child("chatRooms").child(receiverRoom).updateChildren(lastMsgObj);
                                                    scrollMyListViewToBottom();
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
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