package com.application.sampleretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    private final List<Comment> commentList;
    private final Context context;
    public CommentsAdapter(Context context,List<Comment> commentList)
    {
        this.context=context;
        this.commentList=commentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.comments_single_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment=commentList.get(position);
        holder.txtId.setText(String.valueOf(comment.id));
        holder.txtPostId.setText(String.valueOf(comment.postId));
        holder.txtName.setText(comment.name);
        holder.txtEmail.setText(comment.email);
        holder.txtText.setText(comment.text);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtId;
        TextView txtPostId;
        TextView txtName;
        TextView txtEmail;
        TextView txtText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId=itemView.findViewById(R.id.txtId);
            txtPostId=itemView.findViewById(R.id.txtPostId);
            txtName=itemView.findViewById(R.id.txtName);
            txtEmail=itemView.findViewById(R.id.txtEmail);
            txtText=itemView.findViewById(R.id.txtText);
        }
    }
}
