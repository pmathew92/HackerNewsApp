package com.prince.hackernewsapp.ui.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.model.TopStory;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class StoryAdapter extends RealmRecyclerViewAdapter<TopStory, StoryAdapter.StoryViewHolder> {

    private Context context;
    private RealmResults<TopStory> list;

    public StoryAdapter(Context context, @Nullable RealmResults<TopStory> list, boolean autoUpdate) {
        super(list, autoUpdate);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_stories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        final TopStory topStory = list.get(position);
        assert topStory != null;
        holder.mScore.setText(String.valueOf(topStory.getScore()));
        holder.mTitle.setText(topStory.getTitle());
        holder.mUrl.setText(topStory.getUrl());
        holder.mTotalComments.setText(String.valueOf(topStory.getDescendants()));

    }

    class StoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_score)
        TextView mScore;
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_url)
        TextView mUrl;
        @BindView(R.id.tv_time_user)
        TextView mUserTime;
        @BindView(R.id.tv_total_comments)
        TextView mTotalComments;

        StoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
