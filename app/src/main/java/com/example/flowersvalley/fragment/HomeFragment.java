package com.example.flowersvalley.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flowersvalley.model.Banner;
import com.example.flowersvalley.MainActivity;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.adapter.RecyclerViewAdapter;
import com.example.flowersvalley.model.FlowerRecyclerModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<FlowerRecyclerModel> arrFlower;
    private ImageSlider imageSlider;
    private AppCompatTextView view_all;
    private SharedPreferenceManager preferenceManager;
    private static final String TAG = "HomeFragment";
    private DatabaseReference mDatabaseRef;
    private Banner banner;
    private FlowerRecyclerModel flower;
    private FirebaseDatabase firebaseDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);

        preferenceManager = new SharedPreferenceManager(getContext());
        ArrayList<SlideModel> slideModels=new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = firebaseDatabase.getReference("banners");

        imageSlider=view.findViewById(R.id.image_slider);
        imageSlider.setImageList(slideModels);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                slideModels.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    banner = postSnapshot.getValue(Banner.class);
                    Log.i(TAG, "onCreateView: Data > " + postSnapshot.getValue());
                    slideModels.add(new SlideModel("" + banner.getImageUrl(),  ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModels);
            }
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView=view.findViewById(R.id.flower_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        arrFlower=new ArrayList<>();

        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), arrFlower));
        firebaseDatabase.getReference("flowers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrFlower.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    flower = postSnapshot.getValue(FlowerRecyclerModel.class);
                    Log.i(TAG, "onCreateView: Data > " + postSnapshot.getValue());
                    arrFlower.add(flower);
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), arrFlower));

            }
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        view_all=view.findViewById(R.id.view_all);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame, new ViewAllFragment());
                ft.commit();
            }
        });


        return view;


    }
}