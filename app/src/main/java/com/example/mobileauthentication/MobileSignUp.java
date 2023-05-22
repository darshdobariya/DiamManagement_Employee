package com.example.mobileauthentication;


import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MobileSignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String value;
    private TextInputLayout edtPhone, edtOTP;
    private Button verifyOTPBtn, generateOTPBtn, okay_button;
    private String verificationId;
    DatabaseReference myRef;
    ProgressDialog progress;
    Dialog dialog;
    ImageView cancel_button;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mobile_sign_up );

        mAuth = FirebaseAuth.getInstance();

        edtPhone = findViewById(R.id.etMobilenumber);
        edtOTP = findViewById(R.id.etOTP);
        verifyOTPBtn = findViewById(R.id.signUp);
        generateOTPBtn = findViewById(R.id.btnOTP);

        dialog = new Dialog(MobileSignUp.this);

        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtPhone.getEditText().getText().toString())) {
                    Toast.makeText(MobileSignUp.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } if(edtPhone.getEditText().getText().length() == 10) {
                    String phone = "+91" + edtPhone.getEditText().getText().toString();
                    sendVerificationCode(phone);

                    generateOTPBtn.getVisibility();
                }else
                    Toast.makeText( MobileSignUp.this, "Enter valid mobile number", Toast.LENGTH_SHORT ).show();
            }
        });

        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtOTP.getEditText().getText().toString())) {
                    Toast.makeText(MobileSignUp.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    progress = new ProgressDialog(MobileSignUp.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

                    verifyCode(edtOTP.getEditText().getText().toString());
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("Maruti Daim").child( "User" ).child( "Pass" );

                            sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                            String uid  = mAuth.getUid();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("Uid", uid);

                            // Read from the database
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    value = dataSnapshot.child( uid ).child( "Role" ).getValue(String.class);

                                    if (dataSnapshot.hasChild(uid)) {
                                        if (value.equals("Machine") || value.equals("Galaxy") || value.equals("Polish")) {

                                            progress.dismiss();
                                            Intent i = new Intent( MobileSignUp.this, MainActivity.class );
                                            startActivity( i );
                                            finish();
                                        }
                                    }else{
                                        editor.commit();
                                        mAuth.signOut();
                                        progress.dismiss();

                                        dialog.setContentView(R.layout.new_user_dialoge);
                                        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        dialog.setCancelable(false);
                                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                        dialog.show();

                                        cancel_button = dialog.findViewById(R.id.cancel_button);
                                        okay_button = dialog.findViewById(R.id.okay_button);

                                        okay_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MobileSignUp.this, UserRequest.class);
                                                startActivity( intent );
                                                dialog.dismiss();
                                            }
                                        });

                                        cancel_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        Toast.makeText( MobileSignUp.this, "You don't have access for this application", Toast.LENGTH_SHORT ).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });
                        } else {
                            Toast.makeText(MobileSignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)		 // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)				 // Activity (for callback binding)
                        .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                edtOTP.setPlaceholderText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MobileSignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }
}