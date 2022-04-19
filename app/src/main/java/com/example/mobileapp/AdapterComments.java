package com.example.mobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
/*
This class is an adapater for the comments recycler view
 */
public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder>{
    //An arraylist of type Comment, which holds all the comments for a certain post
    private ArrayList<Comment> comments;
    private Context ct;


    public AdapterComments(ArrayList<Comment> comments, Context ct) {
        this.comments = comments;
        this.ct = ct;
    }

    @NonNull
    @Override
    public AdapterComments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.my_comment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewCommentText.setText(comments.get(position).getText());
        Time t = new Time();
        //This displays the Username of the commenter followed by how long ago they made the comment
        holder.textViewCommentUserData.setText(comments.get(position).getUsername() + "  -  "+ t.getApproxTime((long)comments.get(position).getTimestamp()));
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCommentUserData, textViewCommentText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCommentUserData = itemView.findViewById(R.id.textViewCommentUserData);
            textViewCommentText = itemView.findViewById(R.id.textViewCommentText);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
