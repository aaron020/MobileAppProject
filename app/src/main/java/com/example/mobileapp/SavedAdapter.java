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

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.MyViewHolder>{
    private ArrayList<savedLocation> savedLocations;
    private Context ct;
    private OnPostListener mOnPostListener;


    public SavedAdapter(ArrayList<savedLocation> savedLocations,Context ct, OnPostListener onPostListener) {
        this.savedLocations = savedLocations;
        this.ct = ct;
        this.mOnPostListener = onPostListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.my_locations,parent,false);
        return new MyViewHolder(view, mOnPostListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewLocationTitle.setText(savedLocations.get(position).getTitle());
        String coords = "@ " + savedLocations.get(position).getLatitude() + " , " + savedLocations.get(position).getLongitude();
        holder.textViewLocationCoords.setText(coords);
    }

    @Override
    public int getItemCount() {
        return savedLocations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewLocationTitle;
        TextView textViewLocationCoords;


        OnPostListener onPostListener;
        public MyViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);

            textViewLocationTitle = itemView.findViewById(R.id.textViewLocationTitle);
            textViewLocationCoords = itemView.findViewById(R.id.textViewLocationCoords);
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
