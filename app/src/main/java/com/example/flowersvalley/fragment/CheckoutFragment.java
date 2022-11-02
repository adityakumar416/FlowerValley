package com.example.flowersvalley.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flowersvalley.ItemClickListener;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.Utils;
import com.example.flowersvalley.adapter.AddressAdapter;
import com.example.flowersvalley.model.AddressModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CheckoutFragment extends Fragment implements PaymentResultListener {
    private static final String TAG = "CheckoutFragment";
    private AppCompatButton payNow, btnAddNewAddress;
    private SharedPreferenceManager sharedPreferenceManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private RecyclerView addressRecyclerview;
    private ArrayList<AddressModel> addressModels;
    ItemClickListener itemClickListener;
    AddressAdapter addressAdapter;

    public CheckoutFragment() {
        // Required empty public constructor
    }


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
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        payNow = view.findViewById(R.id.pay_now);
        btnAddNewAddress = view.findViewById(R.id.btn_add_new_address);
        addressRecyclerview = view.findViewById(R.id.address_recyclerview);

        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("addresses_list").child(sharedPreferenceManager.getPhone());

        addressModels = new ArrayList<>();
        addressRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));


        // Initialize listener
        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s) {
                // Notify adapter
                addressRecyclerview.post(new Runnable() {
                    @Override
                    public void run() {
                        addressAdapter.notifyDataSetChanged();
                    }
                });
                // Display toast
                Toast.makeText(getContext(), "Selected Id : " + s, Toast.LENGTH_SHORT).show();
            }
        };

        addressAdapter = new AddressAdapter(addressModels, getContext(), itemClickListener);
        addressRecyclerview.setAdapter(addressAdapter);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                addressModels.clear();
                AddressModel addressModel = null;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    addressModel = postSnapshot.getValue(AddressModel.class);
                    Log.i(TAG, "Addresses > " + postSnapshot.getValue());
                    addressModels.add(addressModel);
                }
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.replaceFragment(new AddAddressFragment(), getActivity());
            }
        });
        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payBalAmount = "999";

                // initialize Razorpay account.
                Checkout checkout = new Checkout();
                checkout.setKeyID(getContext().getString(R.string.key_id_ashish_sir));
                checkout.setImage(R.drawable.logo);

                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "Flower Valley");
                    object.put("description", "This payment goes to flowers valley.");
                    object.put("currency", "INR");
                    //int amount = Math.round(Float.parseFloat(payBalAmount) * 100);
                    object.put("amount", 1.0 * 100);
                    object.put("prefill.contact", "" + sharedPreferenceManager.getPhone());
                    object.put("prefill.email", "" + sharedPreferenceManager.getEmail());
                    checkout.open(getActivity(), object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onClick: " + e.getMessage());
                }
            }
        });

        return view;
    }


    @Override
    public void onPaymentSuccess(String s) {
        Log.i(TAG, "onPaymentSuccess: " + s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.i(TAG, "onPaymentError: " + s);
    }
}