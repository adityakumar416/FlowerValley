package com.example.flowersvalley.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flowersvalley.MainActivity;
import com.example.flowersvalley.R;
import com.example.flowersvalley.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class LoginOtpFragment extends Fragment {


    private static final String TAG = "LoginOtpFragment";
    private AppCompatEditText etMobile;
    private AppCompatButton loginbtn;
    private FirebaseAuth mAuth;
    private AppCompatTextView signIn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getContext().getApplicationContext());
        if (getArguments() != null) {
        }
        MainActivity.bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login_otp, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.GONE);

        etMobile=view.findViewById(R.id.mobile);
        loginbtn=view.findViewById(R.id.send_otp_btn);
        signIn = view.findViewById(R.id.sign_in);



        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(false);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMobile.getText().toString().trim().equalsIgnoreCase("")) {
                    Snackbar.make(loginbtn, "Please Enter Mobile Number.", Snackbar.LENGTH_SHORT).show();
                    etMobile.requestFocus();
                } else if (etMobile.getText().toString().length() != 10) {
                    Snackbar.make(loginbtn, "Please Enter Valid Mobile Number.", Snackbar.LENGTH_SHORT).show();
                    etMobile.requestFocus();
                } else {
                    String mobileNumber ="+91"+etMobile.getText().toString().trim();
                    sendOTP(mobileNumber);
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.replaceFragment(new RegistrationPageFragment(), getActivity());
            }
        });


        return view;
    }



    private void sendOTP(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.i(TAG, "onVerificationCompleted: " + phoneAuthCredential.getSmsCode());

                                Toast.makeText(getContext(), "Aapka Code Send ho gya hai. ", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.frame, new HomeFragment());
                                ft.commit();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.i(TAG, "onVerificationFailed: " + e.getMessage());
                                Toast.makeText(getContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Toast.makeText(getContext(), "Aapka Code Send ho gya hai. ", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "onCodeSent: " + s);
                                VerifyOtpFragment otpVerificationFragment = new VerifyOtpFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("token", s);
                                bundle.putString("mobile", mobile);
                                otpVerificationFragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.frame,new HomeFragment());
                                ft.commit();
                                Utils.replaceFragment(otpVerificationFragment, getActivity());

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}