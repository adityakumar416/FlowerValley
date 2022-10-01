package com.example.flowersvalley.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flowersvalley.R;
import com.example.flowersvalley.adapter.RecyclerViewAdapter;
import com.example.flowersvalley.adapter.ViewAllFlowerAdapter;
import com.example.flowersvalley.model.FlowerRecyclerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllFragment extends Fragment {

    private FlowerRecyclerModel flower;
    private  RecyclerView recyclerView;
    private ArrayList<FlowerRecyclerModel> arrFlower;
    private DatabaseReference mDatabaseRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_view_all, container, false);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("flowers");

        recyclerView=view.findViewById(R.id.flower_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        arrFlower=new ArrayList<>();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrFlower.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    flower = postSnapshot.getValue(FlowerRecyclerModel.class);
                    Log.i(TAG, "onCreateView: Data > " + postSnapshot.getValue());
                    arrFlower.add(new FlowerRecyclerModel(flower.getFlowerPrice(),""+flower.getFlowerImageUrl(),""+flower.getFlowerName()));
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), arrFlower));

            }
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(new ViewAllFlowerAdapter(arrFlower,getContext()));


        return view;
    }
}