package com.example.flowersvalley.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.Utils;
import com.example.flowersvalley.model.CartModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<CartModel> list;
    Context context;

    public CartAdapter(ArrayList<CartModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setItems(ArrayList<CartModel> cartModels) {
        this.list = cartModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel cartModel = list.get(position);
        holder.flowerName.setText("" + cartModel.getFlowerName());
        holder.flowerPrice.setText("" + cartModel.getPrice());
        holder.qty.setText("" + cartModel.getFlowerQuantity());

        if (cartModel.getImageUrl() != null) {
            Glide.with(context)
                    .load("" + cartModel.getImageUrl())
                    .into(holder.flowerImage);

        }
        sharedPreferenceManager = new SharedPreferenceManager(context);

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.addToCart(context,cartModel.getFlowerId(), cartModel.getFlowerName(), cartModel.getPrice(), cartModel.getImageUrl(), sharedPreferenceManager.getPhone());
            }
        });
        holder.subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeFromCart(context,cartModel.getFlowerId(), cartModel.getFlowerName(), cartModel.getPrice(), cartModel.getImageUrl(), sharedPreferenceManager.getPhone());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView flowerName, flowerPrice, qty;
        AppCompatImageView flowerImage;
        AppCompatButton addItem, subItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerName = itemView.findViewById(R.id.cart_flower_name);
            flowerPrice = itemView.findViewById(R.id.cart_flower_price);
            qty = itemView.findViewById(R.id.items);
            flowerImage = itemView.findViewById(R.id.cart_flower_img);
            addItem = itemView.findViewById(R.id.add_item);
            subItem = itemView.findViewById(R.id.sub_item);

        }
    }

}
