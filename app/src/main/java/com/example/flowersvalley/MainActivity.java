package com.example.flowersvalley;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flowersvalley.fragment.CartFragment;
import com.example.flowersvalley.fragment.FavouriteFragment;
import com.example.flowersvalley.fragment.HomeFragment;
import com.example.flowersvalley.fragment.LoginOtpFragment;
import com.example.flowersvalley.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    Fragment fragment;
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {



            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_icon:
                        replaceFragment(new HomeFragment());

                        break;
                    case R.id.favourite_icon:
                        replaceFragment(new FavouriteFragment());

                        break;
                    case R.id.cart_icon:
                        replaceFragment(new CartFragment());
                        break;
                    case R.id.profile_icon:
                        replaceFragment(new ProfileFragment());
                        break;

                }

                return true;
            }
        });

        FirebaseApp.initializeApp(getApplicationContext());

        sharedPreferenceManager = new SharedPreferenceManager(this);

        if (sharedPreferenceManager.getName() != null) {
            replaceFragment(new HomeFragment());
        } else {
            replaceFragment(new LoginOtpFragment());
        }


    }

    void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }


}