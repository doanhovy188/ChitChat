package com.example.chitchat.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.models.Message;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;

import com.example.chitchat.R;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagesAdapter extends BaseAdapter {

    AppCompatActivity appCompatActivity;
    ArrayList<Message> messages;

    String senderID, receiverID;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    private int sent_emote = -1;
    private int receive_emote = -1;

    public MessagesAdapter(AppCompatActivity appCompatActivity, ArrayList<Message> messages, String senderID, String receiverID) {
        this.appCompatActivity = appCompatActivity;
        this.messages = messages;
        this.senderID = senderID;
        this.receiverID = receiverID;
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
        if (senderID.equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    int[] reactions = new int[]{
            R.drawable.ic_fb_like,
            R.drawable.ic_fb_love,
            R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_wow,
            R.drawable.ic_fb_sad,
            R.drawable.ic_fb_angry,
            R.drawable.ic_fb_remove_emote
    };

    public void set_emote(int i, View view) {
        Message message = messages.get(i);
        ImageView emote;
        if (getItemViewType(i) == ITEM_RECEIVE) {
            message.setEmote(receive_emote);
            emote = view.findViewById(R.id.receive_emote);
            emote.setImageResource(reactions[receive_emote]);
            receive_emote = -1;
        } else {
            message.setEmote(sent_emote);
            emote = view.findViewById(R.id.sent_emote);
            emote.setImageResource(reactions[sent_emote]);
            sent_emote = -1;
        }

        FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("chatRooms")
                .child(senderID + receiverID)
                .child("messages")
                .child(message.getMessageId()).setValue(message);

        FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("chatRooms")
                .child(receiverID + senderID)
                .child("messages")
                .child(message.getMessageId()).setValue(message);

        emote.setVisibility(View.VISIBLE);
    }

    public void remove_emote(int i, View view) {
        Message message = messages.get(i);
        ImageView emote;
        if (getItemViewType(i) == ITEM_RECEIVE) {
            message.setEmote(-1);
            emote = view.findViewById(R.id.receive_emote);
            emote.setImageResource(reactions[receive_emote]);
            receive_emote = -1;
        } else {
            message.setEmote(-1);
            emote = view.findViewById(R.id.sent_emote);
            emote.setImageResource(reactions[sent_emote]);
            sent_emote = -1;
        }

        FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("chatRooms")
                .child(senderID + receiverID)
                .child("messages")
                .child(message.getMessageId()).setValue(message);

        FirebaseDatabase.getInstance("https://chitchat-3f357-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("chatRooms")
                .child(receiverID + senderID)
                .child("messages")
                .child(message.getMessageId()).setValue(message);

        emote.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        TextView one_message;
        TextView time;
        ImageView emote;

        ReactionsConfig config = new ReactionsConfigBuilder(appCompatActivity)
                .withReactions(reactions).build();

        ReactionPopup popup = new ReactionPopup(appCompatActivity, config, (position) -> {
            if (getItemViewType(i) == ITEM_RECEIVE) {
                receive_emote = position;
            } else {
                sent_emote = position;
            }
            return true; // true is closing popup, false is requesting a new selection
        });

        String strFormatMsg = "HH:mm dd/mm/yyyy";
        String strFormatToCompare = "dd/mm/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(strFormatToCompare);
        String dateMsg = formatter.format(new Date(messages.get(i).getTimesent()));
        String today = formatter.format(new Date().getTime());

        if (dateMsg.equals(today)) strFormatMsg = "HH:mm";

        formatter = new SimpleDateFormat(strFormatMsg);

        if (getItemViewType(i) == ITEM_RECEIVE) {
            view = inflater.inflate(R.layout.item_receive, null);
            one_message = view.findViewById(R.id.receive_message);
            one_message.setText(messages.get(i).getMessage());
            long dif;
            if (i>0)
                dif = messages.get(i).getTimesent() - messages.get(i-1).getTimesent();
            else dif = 300001;
            if (dif > 300000){
                time = view.findViewById(R.id.message_receive_time);
                time.setText(messages.get(i).getTimeSentFormatted(formatter));
            } else {
                ViewGroup parent = (ViewGroup) view;
                time = view.findViewById(R.id.message_receive_time);
                parent.removeView(time);
            }
            if (messages.get(i).getEmote() >= 0) {
                emote = view.findViewById(R.id.receive_emote);
                emote.setImageResource(reactions[messages.get(i).getEmote()]);
                emote.setVisibility(View.VISIBLE);
            }
        } else {
            view = inflater.inflate(R.layout.item_sent, null);
            one_message = view.findViewById(R.id.sent_message);
            one_message.setText(messages.get(i).getMessage());
            long dif;
            if (i>0)
                dif = messages.get(i).getTimesent() - messages.get(i-1).getTimesent();
            else dif = 300001;
            if (dif > 300000){
                time = view.findViewById(R.id.message_sent_time);
                time.setText(messages.get(i).getTimeSentFormatted(formatter));
            }else {
                ViewGroup parent = (ViewGroup) view;
                time = view.findViewById(R.id.message_sent_time);
                parent.removeView(time);
            }

            if (messages.get(i).getEmote() >= 0) {
                emote = view.findViewById(R.id.sent_emote);
                emote.setImageResource(reactions[messages.get(i).getEmote()]);
                emote.setVisibility(View.VISIBLE);
            }
        }

        int id = getItemViewType(i) == ITEM_RECEIVE? R.id.receive_message : R.id.sent_message;

        view.findViewById(id).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popup.onTouch(v, event);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (receive_emote == 6 || sent_emote == 6) {
                            remove_emote(i, (View)v.getParent());

                        }
                        if (receive_emote >= 0 || sent_emote >= 0)
                            set_emote(i, (View)v.getParent());
                    }
                });
                return true;                }
        });


//        view.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                popup.onTouch(view, motionEvent);
//                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        if (receive_emote == 6 || sent_emote == 6) {
//                            remove_emote(i, view);
//                        }
//                        if (receive_emote >= 0 || sent_emote >= 0)
//                            set_emote(i, view);
//                    }
//                });
//                return true;
//            }
//        });

        return view;
    }


}
