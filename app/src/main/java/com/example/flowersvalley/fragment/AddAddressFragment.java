package com.example.flowersvalley.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.Utils;
import com.example.flowersvalley.model.AddressModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddAddressFragment extends Fragment {
    private static final String TAG = "AddAddressFragment";
    private AppCompatButton btnAddAddress;
    private TextInputEditText etName, etHouseNo, etAddress, etMobile, etCity, etDistrict, etState, etPostalCode;
    private SharedPreferenceManager sharedPreferenceManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseRef;

    public AddAddressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        btnAddAddress = view.findViewById(R.id.btn_add_address);
        etName = view.findViewById(R.id.name);
        etHouseNo = view.findViewById(R.id.basic_address);
        etAddress = view.findViewById(R.id.address);
        etMobile = view.findViewById(R.id.mobile);
        etCity = view.findViewById(R.id.city);
        etDistrict = view.findViewById(R.id.district);
        etState = view.findViewById(R.id.state);
        etPostalCode = view.findViewById(R.id.pin_code);


        sharedPreferenceManager = new SharedPreferenceManager(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("addresses_list").child(sharedPreferenceManager.getPhone()).push();


        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, houseNo, address, mobileNumber, city, district, state, postalCode;
                if (etName.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnAddAddress, "Name is Mandatory.", Snackbar.LENGTH_SHORT).show();
                    etName.requestFocus();
                }
                if (etHouseNo.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnAddAddress, "House Number Mandatory.", Snackbar.LENGTH_SHORT).show();
                    etHouseNo.requestFocus();
                } else if (etAddress.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnAddAddress, "Address Mandatory.", Snackbar.LENGTH_SHORT).show();
                    etAddress.requestFocus();
                } else if (etMobile.getText().toString().length() != 10) {
                    Snackbar.make(btnAddAddress, "Please Enter Valid Mobile Number.", Snackbar.LENGTH_SHORT).show();
                    etMobile.requestFocus();
                } else if (etCity.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnAddAddress, "City Mandatory.", Snackbar.LENGTH_SHORT).show();
                    etCity.requestFocus();
                } else if (etDistrict.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnAddAddress, "District Mandatory.", Snackbar.LENGTH_SHORT).show();
                    etDistrict.requestFocus();
                } else if (etState.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnAddAddress, "State Mandatory.", Snackbar.LENGTH_SHORT).show();
                    etState.requestFocus();
                } else if (etPostalCode.getText().toString().length() != 6) {
                    Snackbar.make(btnAddAddress, "Please Enter Valid Postal Code.", Snackbar.LENGTH_SHORT).show();
                    etPostalCode.requestFocus();
                } else {
                    name = etName.getText().toString().trim();
                    houseNo = etHouseNo.getText().toString().trim();
                    address = etAddress.getText().toString().trim();
                    mobileNumber = "+91" + etMobile.getText().toString().trim();
                    city = etCity.getText().toString().trim();
                    district = etDistrict.getText().toString().trim();
                    state = etState.getText().toString().trim();
                    postalCode = etPostalCode.getText().toString().trim();

                    addNewAddress(name, houseNo, address, mobileNumber, city, district, state, postalCode);
                }
            }
        });

        return view;
    }

    private void addNewAddress(String name, String houseNo, String address, String mobileNumber, String city, String district, String state, String postalCode) {
        boolean isSuccess = false;
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddressModel addressModel = new AddressModel(name, houseNo, address, mobileNumber, city, district, state, postalCode);
                addressModel.setAddressId(mDatabaseRef.getKey());
                mDatabaseRef.setValue(addressModel);
                etName.setText(null);
                etHouseNo.setText(null);
                etAddress.setText(null);
                etMobile.setText(null);
                etCity.setText(null);
                etDistrict.setText(null);
                etState.setText(null);
                etPostalCode.setText(null);
                Snackbar.make(btnAddAddress, "Address Added Successfully.", Snackbar.LENGTH_SHORT).show();
                Utils.replaceFragment(new CheckoutFragment(), getActivity());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(btnAddAddress, "Failed, Please Try Again.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}