package com.prince.hackernewsapp.ui.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prince.hackernewsapp.model.realmobject.StoryRealm;

import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

public class StoryAdapter extends RealmRecyclerViewAdapter<StoryRealm, StoryAdapter.StoryViewHolder> {

    private Context context;
    private RealmList<StoryRealm> list;

    public StoryAdapter(Context context, @Nullable RealmList<StoryRealm> list, boolean autoUpdate) {
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {

    }

    class StoryViewHolder extends RecyclerView.ViewHolder {


        StoryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
