package com.example.chitchat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.activities.MainActivity;
import com.example.chitchat.databinding.ItemStoryBinding;
import com.example.chitchat.models.Story;
import com.example.chitchat.models.UserStory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

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
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int i) {
        UserStory userStory = userStories.get(i);
        Picasso.get().load(userStories.get(i).getAvatar()).into(holder.binding.avatar);
        holder.binding.circularStatusView.setPortionsCount(userStories.get(i).getStories().size());
        holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for(Story story: userStory.getStories()){
                    myStories.add(new MyStory(
                            story.getImageURL()
                    ));
                }
                new StoryView.Builder(((MainActivity)mContext).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStory.getName()) // Default is Hidden
                        .setSubtitleText("Damascus") // Default is Hidden
                        .setTitleLogoUrl(userStory.getAvatar()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userStories.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder{
        ItemStoryBinding binding;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStoryBinding.bind(itemView);
        }
    }
}
