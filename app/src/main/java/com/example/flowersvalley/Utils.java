package com.example.flowersvalley;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flowersvalley.fragment.LoginOtpFragment;
import com.example.flowersvalley.fragment.ViewAllFragment;
import com.example.flowersvalley.model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AuthSafe on 19-09-2022.
 * <br>
 * Copyright (c) 2022 SecureLayer7 Technologies. All rights reserved.
 */
public class Utils {
    public static void replaceFragment(Fragment fragment, FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    public static void addToCart(Context context, String flowerId, String flowerName, int price, String imageUrl, String userId) {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("item_in_cart").child(userId).child(flowerId);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utils.getCartItemCount(userId, context);

                if (!snapshot.exists()) {
                    CartModel cartModel = new CartModel(flowerId, flowerName, price, imageUrl, 1);
                    mDatabaseRef.setValue(cartModel);
                }
                for (DataSnapshot snap : snapshot.getChildren()) {
                    CartModel cart = snapshot.getValue(CartModel.class);
                    if (cart.getFlowerId().equalsIgnoreCase(flowerId)) {
                        int qty = cart.getFlowerQuantity();
                        qty++;
                        CartModel cartModel = new CartModel(flowerId, flowerName, price, imageUrl, qty);
                        mDatabaseRef.setValue(cartModel);
                    } else {
                        CartModel cartModel = new CartModel(flowerId, flowerName, price, imageUrl, 1);
                        mDatabaseRef.setValue(cartModel);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void removeFromCart(Context context, String flowerId, String flowerName, int price, String imageUrl, String userId) {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("item_in_cart").child(userId).child(flowerId);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Utils.getCartItemCount(userId, context);
                for (DataSnapshot snap : snapshot.getChildren()) {
                    CartModel cart = snapshot.getValue(CartModel.class);
                    if (cart.getFlowerId().equalsIgnoreCase(flowerId)) {
                        int qty = cart.getFlowerQuantity();
                        if (qty <= 1) {
                            mDatabaseRef.setValue(null);
                            Log.i("TAG", "onDataChange: Cart- Running");
                        } else {
                            qty--;
                            CartModel cartModel = new CartModel(flowerId, flowerName, price, imageUrl, qty);
                            mDatabaseRef.setValue(cartModel);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getCartItemCount(String userId, Context context) {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("item_in_cart").child(userId);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //get data from snapshot
                Log.i("badge", "Total item in cart : " + snapshot.getChildrenCount());

                if (snapshot.getChildrenCount() >= 1) {
                    BottomMenuHelper.showBadge(context, MainActivity.bottomNavigationView, R.id.cart_icon, "" + snapshot.getChildrenCount());
                } else {
                    BottomMenuHelper.removeBadge(MainActivity.bottomNavigationView, R.id.cart_icon);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
