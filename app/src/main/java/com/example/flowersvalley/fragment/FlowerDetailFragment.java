package com.example.flowersvalley.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.example.flowersvalley.MainActivity;
import com.example.flowersvalley.R;
import com.example.flowersvalley.Utils;
import com.example.flowersvalley.adapter.RecyclerViewAdapter;
import com.example.flowersvalley.model.FlowerRecyclerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FlowerDetailFragment extends Fragment {
    private static final String TAG = "FlowerDetailFragment";
    String flowerId,flowerName,flowerPrice,flowerDescription,flowerImageUrl;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    AppCompatTextView flower_name,flower_price,flower_about,back_icon;
    AppCompatImageView flower_image;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flowerId = getArguments().getString("flower_id");
            flowerImageUrl=getArguments().getString("flower_image");
            flowerName=getArguments().getString("flower_name");
            flowerPrice=getArguments().getString("flower_price");
            flowerDescription=getArguments().getString("flower_about");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flower_detail, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
        flower_image=view.findViewById(R.id.detail_flower_image);
        flower_name=view.findViewById(R.id.flower_name);
        flower_price=view.findViewById(R.id.flower_price);
        flower_about=view.findViewById(R.id.flower_about);
        back_icon=view.findViewById(R.id.back_icon);

        Log.i(TAG, "onCreateView: flowerImageUrl "+flowerImageUrl);

        if (flowerImageUrl != null){
            Glide.with(getContext())
                    .load(flowerImageUrl)
                    .into(flower_image);
        }

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.replaceFragment(new HomeFragment(),getActivity());
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();

        Log.i(TAG, "onCreateView: "+flower_image);
        if (flowerId != null) {
            databaseReference = firebaseDatabase.getReference("flowers").child(flowerId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i(TAG, "onDataChange: " + snapshot);
                    flower_name.setText(flowerName);
                    flower_price.setText(flowerPrice);
                    flower_about.setText(flowerDescription);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: " + error);
                }

            });
        } else {
            Toast.makeText(getContext(), "Please try again.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}