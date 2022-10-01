package com.example.flowersvalley.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flowersvalley.MainActivity;
import com.example.flowersvalley.R;
import com.example.flowersvalley.SharedPreferenceManager;
import com.example.flowersvalley.Utils;
import com.example.flowersvalley.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyOtpFragment extends Fragment {
    private static final String TAG = "VerifyOtpFragment";
    private String token,name, email, mobile;
    private AppCompatButton btnVerify;
    private AppCompatEditText etOtp;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public VerifyOtpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString("token");
            name = getArguments().getString("name");
            email = getArguments().getString("email");
            mobile = getArguments().getString("mobile");
        }
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_verify_otp, container, false);


        btnVerify = view.findViewById(R.id.verify_otp_btn);
        etOtp = view.findViewById(R.id.otp_view);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(false);


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtp.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(btnVerify, "Please Enter OTP.", Snackbar.LENGTH_SHORT).show();
                    etOtp.requestFocus();
                } else if (etOtp.getText().toString().length() != 6) {
                    Snackbar.make(btnVerify, "Please Enter 6 Digit OTP.", Snackbar.LENGTH_SHORT).show();
                    etOtp.requestFocus();
                } else {
                    String otp = etOtp.getText().toString().trim();
                    verifyOtp(otp, token);
                }
            }
        });


        return view;
    }
    private void verifyOtp(String otp, String token) {
        Log.i(TAG, "verifyOtp: "+otp+" "+token);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(token, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");




                            Log.i(TAG, "verifyOtp: Name " + name);
                            Log.i(TAG, "verifyOtp: Email " + email);
                            Log.i(TAG, "verifyOtp: Mobile " + mobile);


                            if (name != null && email != null && mobile != null) {
                                User user = new User("" + name, "" + email, "" + mobile);

                                databaseReference = firebaseDatabase.getReference("users").child(mobile);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.setValue(user);
                                        Log.i(TAG, "onDataChange: " + snapshot);
                                        if (snapshot.exists()){
                                            Snackbar.make(btnVerify,"Registration Successfully",Snackbar.LENGTH_SHORT).show();
                                            Utils.replaceFragment(new LoginOtpFragment(), getActivity());
                                        }
                                        else {
                                            Snackbar.make(btnVerify,"Something went Wrong",Snackbar.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e(TAG, "onCancelled: " + error);
                                    }
                                });
                            }else {
                                Toast.makeText(getContext(), "From Login Fragment", Toast.LENGTH_SHORT).show();
                                databaseReference = firebaseDatabase.getReference("users").child(mobile);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        Log.i(TAG, "Login onDataChange Name: " + snapshot.child("name").getValue().toString());
                                        Log.i(TAG, "Login onDataChange Email: " + snapshot.child("email").getValue().toString());
                                        Log.i(TAG, "Login onDataChange Mobile: " + snapshot.child("mobile").getValue().toString());


                                        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(getContext());
                                        sharedPreferenceManager.setName(snapshot.child("name").getValue().toString());
                                        sharedPreferenceManager.setEmail(snapshot.child("email").getValue().toString());
                                        sharedPreferenceManager.setPhone(snapshot.child("mobile").getValue().toString());

                                        Utils.replaceFragment(new HomeFragment(), getActivity());

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e(TAG, "onCancelled: " + error);
                                    }
                                });
                            }
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Invalid OTP.", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}