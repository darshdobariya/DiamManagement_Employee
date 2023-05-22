package com.example.mobileauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;

public class UserRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ImageView imageProfile;
    TextInputEditText mobileProfile, nameProfile, stringSelectedBDate;
    Spinner spinner;
    Button submit;
    String stringImageProfile, stringMobileProfile, stringNameProfile, stringRole,   string1Date, uid;
    String[] role = { "Machine", "Polish", "Galaxy" };
    SharedPreferences sharedPreferences;
    DatabaseReference myRef;
    int SELECT_PICTURE = 200;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_request );

        // Set ID
        imageProfile = findViewById( R.id.imageProfile );
        stringSelectedBDate = findViewById( R.id.selectedBDate );
        mobileProfile = findViewById( R.id.mobileProfile );
        nameProfile = findViewById( R.id.nameProfile );
        spinner = findViewById( R.id.roleProfile );
        submit = findViewById( R.id.submit );

        // Get User Data
        sharedPreferences = this.getSharedPreferences( "User", MODE_PRIVATE );
        String restoredText = sharedPreferences.getString( "Uid", null );
        uid = sharedPreferences.getString( "User", restoredText );

        // Storage Firebase
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images/" + uid + "/");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference( "Maruti Daim" ).child( "User" ).child( "Pending" ).child( uid );

        // Spinner
        spinner.setOnItemSelectedListener( (AdapterView.OnItemSelectedListener) UserRequest.this );
        ArrayAdapter ad = new ArrayAdapter( this, android.R.layout.simple_spinner_item, role);
        ad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

        // Selecting Birth Date
        stringSelectedBDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserRequest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                stringSelectedBDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                string1Date = stringSelectedBDate.getText().toString();
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        // Pick Image into mobile storage
        imageProfile.setOnClickListener( v->{
            imageChooser();
        } );

        submit.setOnClickListener( v->{
            if (stringImageProfile.length() >= 2 ) {
                stringNameProfile = nameProfile.getText().toString();
                if (stringNameProfile.length() >= 2){
                    stringMobileProfile = mobileProfile.getText().toString();
                    if (stringMobileProfile.length() == 10){
                        System.out.println("------------" + string1Date );
                        if (stringSelectedBDate.getText().toString().equals( "" ) ){
                            Toast.makeText( this, "Select date", Toast.LENGTH_SHORT ).show();
                        }else {
                            if (restoredText != null) {
                                myRef.child( "Name" ).setValue( stringNameProfile );
                                myRef.child( "BirthDate" ).setValue( string1Date );
                                myRef.child( "Mobile" ).setValue( "+91" + stringMobileProfile );
                                myRef.child( "Role" ).setValue( stringRole );
                                myRef.child( "Uid" ).setValue( uid );

                                uploadImage();

                                progressDialog.dismiss();
                            }
                        }
                    }else mobileProfile.setError( "Enter mobile no" );
                }else nameProfile.setError( "Enter name" );
            }else Toast.makeText( UserRequest.this, "Choose valid photo", Toast.LENGTH_LONG ).show();
        } );
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        stringRole = role[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                stringImageProfile = String.valueOf( data.getData() );
                if (null != selectedImageUri) {
                    imageProfile.setImageURI(selectedImageUri);
                }
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (imageProfile != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageReference.putFile( Uri.parse( stringImageProfile ) );
            storageReference.putFile( Uri.parse( stringImageProfile ) ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(UserRequest.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage( "Uploaded " + (int)progress + "%");
                }
            });
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    myRef.child( "Picture" ).setValue( String.valueOf( uri ) );
                    progressDialog.dismiss();
                    Toast.makeText( UserRequest.this, "Your application in under verifying", Toast.LENGTH_SHORT ).show();

                    Intent intent = new Intent( UserRequest.this, ThankYou.class);
                    startActivity( intent );
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText( UserRequest.this, "image not downloaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}