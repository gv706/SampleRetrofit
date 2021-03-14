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
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private final List<Post> postList;
    private final Context context;
    public MyAdapter(Context context,List<Post> postList)
    {
        this.context=context;
        this.postList=postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post=postList.get(position);
        holder.txtId.setText(String.format(Locale.getDefault(),"Id:%d", post.id));
        holder.txtUserId.setText(String.format(Locale.getDefault(),"userId:%d", post.userId));
        holder.txtTitle.setText(post.title);
        holder.txtText.setText(post.text);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtId;
        TextView txtUserId;
        TextView txtTitle;
        TextView txtText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId=itemView.findViewById(R.id.txtId);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtUserId=itemView.findViewById(R.id.txtUserId);
            txtText=itemView.findViewById(R.id.txtText);
        }
    }
}
