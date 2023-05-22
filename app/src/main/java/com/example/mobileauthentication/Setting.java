package com.example.mobileauthentication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class Setting extends AppCompatActivity {

    ImageView instagram, twitter, facebook, linkedin, profileImage;
    LinearLayout logout;
    TextView userName, userRole, userMobileNo, userBDate;
    String linkInstagram, linkTwitter, linkFacebook, linkLinkedin, linkProfileImage, linkUserName, linkUserRole, linkUserMobileNo, linkUserBDate;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference myRef, myRef1;
    FirebaseDatabase database;

    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );
        // Set id
        instagram = findViewById( R.id.instagram );
        twitter = findViewById( R.id.twitter );
        facebook = findViewById( R.id.facebook );
        linkedin = findViewById( R.id.linkedin );
        profileImage = findViewById( R.id.profileImage );
        logout = findViewById( R.id.logOut );
        userName = findViewById( R.id.profileName );
        userRole = findViewById( R.id.profileRole );
        userMobileNo = findViewById( R.id.mobileNo );
        userBDate = findViewById( R.id.birthDate );

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference( "Maruti Daim" ).child( "Social Media" );
        myRef1 = database.getReference( "Maruti Daim" ).child( "User" ).child( "Pass" ).child( mAuth.getUid() );

        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();

        instagram.setOnClickListener( v -> {
            if (linkInstagram == null) {
                Toast.makeText( Setting.this, "Data not Loaded Press again", Toast.LENGTH_SHORT ).show();
            } else Chrome( linkInstagram );
        } );

        twitter.setOnClickListener( v -> {
            if (linkTwitter == null) {
                Toast.makeText( Setting.this, "Data not Loaded Press again", Toast.LENGTH_SHORT ).show();
            } else Chrome( linkTwitter );
        } );

        facebook.setOnClickListener( v -> {
            if (linkFacebook == null) {
                Toast.makeText( Setting.this, "Data not Loaded Press again", Toast.LENGTH_SHORT ).show();
            } else Chrome( linkFacebook );
        } );

        linkedin.setOnClickListener( v -> {
            if (linkLinkedin == null) {
                Toast.makeText( Setting.this, "Data not Loaded Press again", Toast.LENGTH_SHORT ).show();
            } else Chrome( linkLinkedin );
        } );

        logout.setOnClickListener( v -> {
            mAuth.signOut();
        } );

        try {
            // auto change image
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    userName.setText( linkUserName );
                    userRole.setText( linkUserRole );
                    userMobileNo.setText( linkUserMobileNo );
                    userBDate.setText( linkUserBDate );

                    Picasso.get().load(linkProfileImage).into(profileImage);
                    System.out.println("----------" + userBDate);
                    if (userBDate.getText().length() >= 1 ){
                        p.dismiss();
                        System.out.println("----------" + linkProfileImage);
                    }
                }
            };

            Timer timer = new Timer();
            timer.schedule( new TimerTask() {
                @Override
                public void run() {
                    handler.post( update );
                }
            }, 1000, 5 );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Chrome(String link){
        Uri uri = Uri.parse( link );
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private class AsyncTaskExample extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog( Setting.this );
            p.setMessage( "Data Loading...." );
            p.setIndeterminate( true );
            p.setCancelable( true );
            p.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Read profile data from the database
            myRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("xcxc::::"+dataSnapshot);

                    linkProfileImage = dataSnapshot.child("Picture").getValue(String.class);
                    linkUserName = dataSnapshot.child("Name").getValue(String.class);
                    linkUserRole = dataSnapshot.child("Role").getValue(String.class);
                    linkUserMobileNo = dataSnapshot.child("Mobile").getValue(String.class);
                    linkUserBDate = dataSnapshot.child("BirthDate").getValue(String.class);

                    // Profile image loading
                    linkProfileImage = dataSnapshot.child( "Picture" ).getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("xcxc::::"+dataSnapshot);
                    linkInstagram = dataSnapshot.child( "Instagram" ).getValue(String.class);
                    linkTwitter = dataSnapshot.child( "Twitter" ).getValue(String.class);
                    linkFacebook = dataSnapshot.child( "Facebook" ).getValue(String.class);
                    linkLinkedin = dataSnapshot.child( "Linkedin" ).getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute( unused );
        }
    }
}