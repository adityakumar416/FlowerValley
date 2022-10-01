package com.example.flowersvalley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowersvalley.R;
import com.example.flowersvalley.model.FlowerRecyclerModel;

import java.util.ArrayList;

public class ViewAllFlowerAdapter extends RecyclerView.Adapter<ViewAllFlowerAdapter.ViewHolder> {


    ArrayList<FlowerRecyclerModel> arrFlower;
    Context context;

    public ViewAllFlowerAdapter(ArrayList<FlowerRecyclerModel> arrFlower, Context context) {
        this.arrFlower = arrFlower;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_recycler_view,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllFlowerAdapter.ViewHolder holder, int position) {
        FlowerRecyclerModel flowerRecyclerModel=arrFlower.get(position);


        holder.flower_name.setText(""+flowerRecyclerModel.getFlowerName());
        holder.flower_price.setText(""+flowerRecyclerModel.getFlowerPrice());
        Glide.with(context)
                .load(flowerRecyclerModel.getFlowerImageUrl())
                .into(holder.flower_img);
    }

    @Override
    public int getItemCount() {
        return arrFlower.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView flower_img;
        AppCompatTextView flower_name,flower_price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flower_img=itemView.findViewById(R.id.flower_image);
            flower_name =itemView.findViewById(R.id.flower_name);
            flower_price =itemView.findViewById(R.id.flower_price);

        }
    }
}

