package com.example.mobileauthentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mobileauthentication.Demo.DataAdd;
import com.example.mobileauthentication.home.HomeFragment;
import com.example.mobileauthentication.profile.ProfileFragment;
import com.example.mobileauthentication.work.WorkFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    int a = 0, b=0;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView( R.layout.activity_main );

        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_favorites:
                    selectedFragment = new WorkFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity( new Intent( this, MobileSignUp.class ) );
        }

        if (b == 0){
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Maruti Daim").child( "User" ).child( "Pass" );
            dbRef.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ProgressDialog progressDialog = new ProgressDialog( MainActivity.this );
                    progressDialog.setMessage( "Wait..." );
                    progressDialog.show();

                    for (DataSnapshot snapshot1: snapshot.getChildren()) {
                        if (user.getUid().equals( snapshot1.getKey())){
                            a += 1;
                        }
                    }
                    if (a == 1){
                        progressDialog.dismiss();
                        b = 1;
                    }else {
                        Toast.makeText( MainActivity.this, "Sorry", Toast.LENGTH_SHORT ).show();
                        progressDialog.dismiss();
                        startActivity( new Intent( MainActivity.this, MobileSignUp.class ) );
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            } );
        }
    }
}