package com.example.flowersvalley.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.flowersvalley.Utils;
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
    private SearchView searchView;
    private ListView searchFlowerList;
    private RelativeLayout mainLayout;


    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        preferenceManager = new SharedPreferenceManager(getContext());
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = firebaseDatabase.getReference("banners");

        view_all = view.findViewById(R.id.view_all);
        searchView = view.findViewById(R.id.search_bar);
        searchFlowerList = view.findViewById(R.id.search_list);
        mainLayout=view.findViewById(R.id.main_layout);
        imageSlider = view.findViewById(R.id.image_slider);
        imageSlider.setImageList(slideModels);
        arrFlower = new ArrayList<>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                slideModels.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    banner = postSnapshot.getValue(Banner.class);
                    Log.i(TAG, "onCreateView: Data > " + postSnapshot.getValue());
                    slideModels.add(new SlideModel("" + banner.getImageUrl(), ScaleTypes.FIT));
                }
                imageSlider.setImageList(slideModels);
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView = view.findViewById(R.id.flower_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));


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




        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                Utils.replaceFragment(new ViewAllFragment(), getActivity());
            }
        });

        ArrayAdapter<FlowerRecyclerModel> flowerArrayAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrFlower);
        searchFlowerList.setAdapter(flowerArrayAdapter);
        searchFlowerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //flower = flowers.get(i);
                Log.i(TAG, "onItemSelected: " + arrFlower.get(i).getFlowerId());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e(TAG, "onNothingSelected:" );
            }
        });
        searchFlowerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemSelected: " + arrFlower.get(i).getFlowerId());
            }
        });
        searchView.setQueryHint("Red Rose");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: " + query);
                if (arrFlower.contains(query)) {
                    flowerArrayAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + newText);
                searchFlowerList.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                flowerArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                searchFlowerList.setVisibility(View.GONE);
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);

                mainLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        Utils.getCartItemCount(preferenceManager.getPhone(), getContext());

        return view;
    }
}