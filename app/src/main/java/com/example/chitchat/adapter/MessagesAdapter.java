package com.example.chitchat.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Date;

import kotlin.jvm.functions.Function1;

public class MessagesAdapter extends BaseAdapter {

    AppCompatActivity appCompatActivity;
    ArrayList<Message> messages;

    String user_loggedIn_ID;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    private int sent_emote = -1;
    private int receive_emote = -1;

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

    int[] reactions = new int[]{
            R.drawable.ic_fb_like,
            R.drawable.ic_fb_love,
            R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_wow,
            R.drawable.ic_fb_sad,
            R.drawable.ic_fb_angry
    };

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        TextView one_message;
        TextView time;

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

        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popup.onTouch(view, motionEvent);
                if (getItemViewType(i) == ITEM_RECEIVE && receive_emote >= 0) {
                    ImageView emote = view.findViewById(R.id.receive_emote);
                    emote.setImageResource(reactions[receive_emote]);
                    receive_emote = -1;
                    emote.setVisibility(View.VISIBLE);
                }
                if (getItemViewType(i) == ITEM_SENT && sent_emote >= 0) {
                    ImageView emote = view.findViewById(R.id.sent_emote);
                    emote.setImageResource(reactions[sent_emote]);
                    sent_emote = -1;
                    emote.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        return view;
    }
}
