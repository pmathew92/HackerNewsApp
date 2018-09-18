package com.prince.hackernewsapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.model.Comments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private Context context;
    private List<Comments> commentsList;

    public CommentsAdapter(Context context, List<Comments> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comments comment = commentsList.get(position);
        holder.comments.setText(comment.getText());

        long time = comment.getTime();

        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yyyy - HH:mm", Locale.getDefault());

        holder.userTime.setText(String.format("%s . %s", simpleDateFormat.format(date), comment.getBy()));
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_comment)
        TextView comments;

        @BindView(R.id.tv_user_time)
        TextView userTime;

        CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
