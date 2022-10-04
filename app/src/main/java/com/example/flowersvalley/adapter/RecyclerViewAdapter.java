package com.example.flowersvalley.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowersvalley.BottomMenuHelper;
import com.example.flowersvalley.MainActivity;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.Utils;
import com.example.flowersvalley.fragment.FlowerDetailFragment;
import com.example.flowersvalley.model.FlowerRecyclerModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    SharedPreferenceManager sharedPreferenceManager;

    ArrayList<FlowerRecyclerModel> arrFlower;
    Context context;
    int count = 1;
    private String flower_name,flower_price,flower_about;


    public RecyclerViewAdapter(Context context, ArrayList<FlowerRecyclerModel> arrFlower)
    {
        this.arrFlower=arrFlower;
        this.context=context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_card_recyclerview,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlowerRecyclerModel flowerRecyclerModel=arrFlower.get(position);


        holder.flower_name.setText(flowerRecyclerModel.getFlowerName());
        holder.flower_price.setText(flowerRecyclerModel.getFlowerPrice());

        Glide.with(context)
                .load(flowerRecyclerModel.getFlowerImageUrl())
                .into(holder.flower_img);


        sharedPreferenceManager = new SharedPreferenceManager(context);

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sharedPreferenceManager.setItemCounter(count++);
                BottomMenuHelper.showBadge(context, MainActivity.bottomNavigationView, R.id.cart_icon, "" + sharedPreferenceManager.getItemCounter());


            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlowerDetailFragment fragment = new FlowerDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("flower_id", flowerRecyclerModel.getFlowerId());
                bundle.putString("flower_name",flowerRecyclerModel.getFlowerName());
                bundle.putString("flower_price",flowerRecyclerModel.getFlowerPrice());
                bundle.putString("flower_about",flowerRecyclerModel.getFlowerDescription());
                bundle.putString("flower_image",flowerRecyclerModel.getFlowerImageUrl());
                fragment.setArguments(bundle);
                Utils.replaceFragment(fragment, (FragmentActivity) context);
            }
        });

    }

    @Override
    public int getItemCount() {



        return arrFlower.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView flower_img;
        AppCompatTextView flower_name,flower_price;
        CardView cardView;
        AppCompatImageButton addItem;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flower_img=itemView.findViewById(R.id.flower_image);
            flower_name =itemView.findViewById(R.id.flower_name);
            flower_price =itemView.findViewById(R.id.flower_price);
            cardView = itemView.findViewById(R.id.card);
            addItem = itemView.findViewById(R.id.btn_add_item);

        }
    }
}
