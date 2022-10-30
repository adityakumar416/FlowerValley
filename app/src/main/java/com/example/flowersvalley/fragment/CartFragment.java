package com.example.flowersvalley.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flowersvalley.MainActivity;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.adapter.CartAdapter;
import com.example.flowersvalley.model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";
    private RecyclerView cartRecyclerview;
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase firebaseDatabase;
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<CartModel> cartModels;


    public CartFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerview = view.findViewById(R.id.cart_recyclerview);

        cartModels = new ArrayList<>();
        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("item_in_cart").child(sharedPreferenceManager.getPhone());

        cartRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        CartAdapter cartAdapter = new CartAdapter(cartModels, getContext());
        cartRecyclerview.setAdapter(cartAdapter);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartModels.clear();
                CartModel cartModel = null;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    cartModel = postSnapshot.getValue(CartModel.class);
                    Log.i(TAG, "cart Data > " + postSnapshot.getValue());
                    cartModels.add(cartModel);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}