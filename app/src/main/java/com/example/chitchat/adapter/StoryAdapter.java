package com.example.chitchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.databinding.ItemStoryBinding;
import com.example.chitchat.models.UserStory;

import java.util.ArrayList;

public class StoryAdapter extends  RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    Context mContext;
    ArrayList<UserStory> userStories;

    public StoryAdapter(Context context, ArrayList<UserStory> userStories){
        mContext = context;
        this.userStories = userStories;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder{
        ItemStoryBinding binding;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStoryBinding.bind(itemView);
        }
    }
}
