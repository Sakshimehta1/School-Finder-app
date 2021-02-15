package com.example.user_school;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_recViewSchool_list extends FirestoreRecyclerAdapter<data_schools, Adapter_recViewSchool_list.MyViewHolder> {

    private onItemClickListener listener;
    public Adapter_recViewSchool_list(@NonNull FirestoreRecyclerOptions<data_schools> options,onItemClickListener listener) {
        super(options);
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull data_schools data_schools) {

        holder.name.setText(data_schools.getName());
        holder.location.setText(data_schools.getAddress());
        holder.ratings.setText(String.format("%.02f", data_schools.getShow_ratings()));
        if(data_schools.getPhotos()!=null)
        {
            String photo;
            photo=data_schools.getPhotos().get(0);
            Picasso.get().load(photo).into(holder.imageView);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_schools_list, parent, false);
        return new MyViewHolder(view);
    }

     class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,location,ratings;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.schname);
            location = itemView.findViewById(R.id.schloc);
            imageView=itemView.findViewById(R.id.imgview);
            ratings=itemView.findViewById(R.id.rate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(listener!=null)
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                }
            });
        }
    }
    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener)
    {
        this.listener=listener;
    }


}
