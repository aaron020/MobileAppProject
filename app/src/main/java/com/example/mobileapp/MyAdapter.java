package com.example.mobileapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<Post> posts;
//    private ArrayList<String> usernames;
    private Context ct;
    private int[] typeOfPosts = {R.mipmap.ic_helpful_round, R.mipmap.ic_informative_round, R.mipmap.ic_curious_round,
                                R.mipmap.ic_event_round, R.mipmap.ic_academic_round, R.mipmap.ic_weather_round, R.mipmap.ic_otheroption_round};
    private OnPostListener mOnPostListener;


    public MyAdapter(ArrayList<Post> posts,Context ct, OnPostListener onPostListener) {
        this.posts = posts;
        this.ct = ct;
        this.mOnPostListener = onPostListener;
//        userIdToUsername();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view, mOnPostListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewTitle.setText(posts.get(position).getTitle());
        holder.textViewDescription.setText(posts.get(position).getShortDesc());
        Time t = new Time();
        String usernameTime = posts.get(position).getUsername() + "   -   " + t.getApproxTime(posts.get(position).getTimestamp());
        Distance d = new Distance();
        String distance = d.distanceToString(Location.latitude,Location.longitude,posts.get(position).getLatitude(),posts.get(position).getLongitude());
        holder.textViewLocation.setText(distance);
        holder.textViewUserData.setText(usernameTime);
        holder.PostImage.setImageResource(typeOfPosts[posts.get(position).getTypeChosen()-1]);
    }

//    private ArrayList<Post> manageDistance(ArrayList<Post> posts_In){
//        Distance dist = new Distance();
//        ArrayList<Post> postsArranged = new ArrayList<>();
//        for(int i = 0; i < posts_In.size(); i++){
//            if(dist.distance(Location.latitude, Location.longitude, posts_In.get(i).getLatitude(),posts_In.get(i).getLongitude()) <= Settings.Distance){
//                postsArranged.add(posts_In.get(i));
//            }
//        }
//        return postsArranged;
//    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewUserData;
        TextView textViewLocation;
        ImageView PostImage;


        OnPostListener onPostListener;
        public MyViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewUserData = itemView.findViewById(R.id.textViewUserData);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            PostImage = itemView.findViewById(R.id.PostImage);
            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostListener{
        void onPostClick(int position);
    }

}
