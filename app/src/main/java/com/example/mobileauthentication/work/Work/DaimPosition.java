package com.example.mobileauthentication.work.Work;

import static android.view.View.INVISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.mobileauthentication.Demo.DaimProcess;
import com.example.mobileauthentication.Demo.GMPUser;
import com.example.mobileauthentication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DaimPosition extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView status_image;
    String DaimNo, RuffWeight, Status, KapanNo, Role;
    DatabaseReference mRef;
    EditText TotalPis;
    EditText ReadyPis;
    EditText ReadyRuff;
    TextView kapanDaimNo;
    TextView ruffWeight;
    TextView breakPis;
    TextView Date;
    TextView sendTo;
    Spinner select;
    ArrayList<String> role, name1, uid1, mobile1, areasAdapter;
    int position, total, ready, breaked;
    Button addButton;
    String[] department = { "Galaxy", "Machine", "Polish" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_daim_position );

        Intent intent = getIntent();
        DaimNo = intent.getStringExtra("DaimNo");
        RuffWeight = intent.getStringExtra("RuffWeight");
        Status = intent.getStringExtra("Status");
        KapanNo = intent.getStringExtra( "KapanNo" );
        Role = intent.getStringExtra( "Role" );

        kapanDaimNo = findViewById( R.id.kapanDaimNo );
        ruffWeight = findViewById( R.id.ruffWeight );
        breakPis = findViewById( R.id.breakPis );
        TotalPis = findViewById( R.id.totalPis );
        ReadyPis = findViewById( R.id.readyPis );
        Date = findViewById( R.id.date );
        sendTo = findViewById( R.id.sendTo );
        kapanDaimNo = findViewById( R.id.kapanDaimNo );
        ReadyRuff = findViewById( R.id.readyWeight );
        addButton = findViewById( R.id.addData );
        status_image = findViewById( R.id.status_image );

        if (Status.equals( "Pending" )){
            status_image.setImageResource(R.drawable.waiting);
            ArrayAdapter ad = new ArrayAdapter( DaimPosition.this, android.R.layout.simple_spinner_item, department);
            ad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            select.setAdapter(ad);

            getDateTime();
            CommonProp();
            Date.setOnClickListener( v->{DatePick();} );

        }else if(Status.equals( "Pass" )) {
            Pass();
            CommonProp();
        }

        addButton.setOnClickListener( v-> {
            if(TotalPis.getText().toString().length() == 0 && ReadyPis.getText().toString().length() == 0){
                Toast.makeText( this, "Enter valid Value", Toast.LENGTH_SHORT ).show();
            }else {
                ready = Integer.parseInt( ReadyPis.getText().toString() );
                total = Integer.parseInt( TotalPis.getText().toString() );
                addData();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Maruti Daim").child("User").child( "Pass" );
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name1 = new ArrayList<String>();
                uid1 = new ArrayList<String>();
                mobile1 = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    GMPUser studentModel = areaSnapshot.getValue( GMPUser.class );

                    if(Role.equals("Galaxy") && studentModel.getRole().equals( "Machine" )){
                        System.out.println("-------------" + areaSnapshot);
                        name1.add(studentModel.getName());
                        uid1.add( studentModel.getUid() );
                        mobile1.add( studentModel.getMobile() );
                    }
                }

                select = findViewById( R.id.spinner2 );
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(DaimPosition.this, android.R.layout.simple_spinner_item, name1);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select.getSelectedItemPosition();
//                select.getSelectedItem();
                select.setAdapter(areasAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date1 = new Date();
        Date.setText( dateFormat.format(date1) );
        return dateFormat.format(date1);
    }

    private void DatePick(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog( DaimPosition.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addData(){
        breaked = total - ready;
        breakPis.setText( String.valueOf( breaked ) );

        mRef = FirebaseDatabase.getInstance().getReference("Maruti Daim").child( "Kapan" ).child( KapanNo ).child( DaimNo );
        mRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (Status.equals( "Pending" )) {
                    mRef.child( "Manufacture" ).child( Role ).child( "EndDate" ).setValue( Date.getText().toString() );
                    mRef.child("Manufacture").child(Role).child( "Less" ).setValue( breaked );
                    mRef.child("Manufacture").child(Role).child( "Ready" ).setValue( ready );
                    mRef.child("Manufacture").child(Role).child( "ReadyWeight" ).setValue( ReadyRuff.getText().toString() );
                    mRef.child("Manufacture").child(Role).child( "SendTo" ).setValue( sendTo.getText().toString() );
                    mRef.child("Manufacture").child(Role).child( "Status" ).setValue( "Pass" );

                    if (Role.equals( "Galaxy" )){
                        mRef.child( "TotalPic" ).setValue( total );
                        mRef.child( "Manufacture" ).child(sendTo.getText().toString()).child( "StartDate" ).setValue( Date.getText().toString() );
                        mRef.child("Manufacture").child(sendTo.getText().toString()).child( "User" ).child( "Mobile" ).setValue( mobile1.get(select.getSelectedItemPosition()) );
                        mRef.child("Manufacture").child(sendTo.getText().toString()).child( "User" ).child( "Name" ).setValue( name1.get(select.getSelectedItemPosition()) );
                        mRef.child("Manufacture").child(sendTo.getText().toString()).child( "User" ).child( "Uid" ).setValue( uid1.get(select.getSelectedItemPosition()) );
                    }else Toast.makeText( DaimPosition.this, "Daim already completed...", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

    private void CommonProp(){
        ruffWeight.setText( RuffWeight );
        kapanDaimNo.setText( KapanNo + "-" +  DaimNo );

        if (Role.equals( "Galaxy" )){
            sendTo.setText( "Machine" );
        }else if (Role.equals( "Machine" )){
            sendTo.setText( "Polish" );
        }else sendTo.setText( "Admin" );
    }

    private void Pass(){
        addButton.setVisibility( INVISIBLE );

        DatabaseReference d1 = FirebaseDatabase.getInstance().getReference("Maruti Daim").child( "Kapan" ).child( KapanNo ).child( DaimNo );
        d1.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                TotalPis.setText(String.valueOf( snapshot.child( "TotalPic" ).getValue() ));
                ReadyPis.setText(String.valueOf( snapshot.child( "Manufacture" ).child( Role ).child( "Ready" ).getValue() ));
                ReadyRuff.setText(String.valueOf( snapshot.child( "Manufacture" ).child( Role ).child( "ReadyWeight" ).getValue() ));
                Date.setText(String.valueOf( snapshot.child( "Manufacture" ).child( Role ).child( "EndDate" ).getValue() ));
                breakPis.setText( String.valueOf( snapshot.child( "Manufacture" ).child( Role ).child( "Less" ).getValue() ) );

                TotalPis.setEnabled( false );
                ReadyPis.setEnabled( false );
                ReadyRuff.setEnabled( false );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        status_image.setImageResource( R.drawable.complete );
    }
}