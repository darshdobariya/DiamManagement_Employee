package com.example.mobileauthentication.work.Work;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileauthentication.Demo.DaimProcess;
import com.example.mobileauthentication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaimInfo extends AppCompatActivity {

    String Kapan, Role;
    TextView kapanNo, role, quantity, totalWeight, ratio, ready;
    DaimProcess daimProcess1;
    RecyclerView recyclerView;
    DaimInfoAdapter daim_listAdaptor;
    List<DaimProcess> daim_list;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    double totalWeight1, readyWeight1;
    int quantity1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_daiminfo );

        recyclerView = findViewById( R.id.rc_infoDaim );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.getLayoutManager().setMeasurementCacheEnabled( false );
        daim_list = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();

        kapanNo = findViewById( R.id.kapanNo );
        role = findViewById( R.id.role );
        quantity = findViewById( R.id.quantity );
        totalWeight = findViewById( R.id.totalWeight );
        ratio = findViewById( R.id.ratio );
        ready = findViewById( R.id.totalOutput );

        Intent intent = getIntent();
        Kapan = intent.getStringExtra( "Kapan" );
        Role = intent.getStringExtra( "Role" );
        kapanNo.setText( Kapan );
        role.setText( Role );

        getdata();
    }

    public void getdata() {
        if (Role.equals( "Galaxy" )){
            databaseReference = FirebaseDatabase.getInstance().getReference("Maruti Daim").child( "Kapan" ).child( Kapan );
            databaseReference.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            quantity1 += 1;
                            quantity.setText( String.valueOf( quantity1 ) );
                            String no = snapshot1.getKey();//d1
                            String w1 = String.valueOf( snapshot1.child( "RuffWeight" ).getValue() );

                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {

                                    String start = String.valueOf( snapshot3.child( "StartDate" ).getValue() );
                                    String w2 = String.valueOf( snapshot3.child( "ReadyWeight" ).getValue() );
                                    String status = String.valueOf( snapshot3.child( "Status" ).getValue() );
                                    String sendto = String.valueOf( snapshot3.child( "SendTo" ).getValue() );

                                    if (String.valueOf( snapshot3.child( "User" ).child( "Uid" ).getValue() ).equals( firebaseAuth.getUid() ) )
                                    {
                                        if (no.isEmpty() || start.isEmpty() || w1.isEmpty() || w2.isEmpty() || status.isEmpty() || sendto.isEmpty()) {
                                            Toast.makeText( DaimInfo.this, "Wait for some time...", Toast.LENGTH_SHORT ).show();
                                        }else {
                                            daimProcess1 = new DaimProcess( no, start, w1, w2, status, sendto );
                                            daim_list.add( daimProcess1 );

                                            totalWeight1 += Double.parseDouble( w1 );
                                            if (w2 == null){
                                                Log.d( TAG, "onDataChange: null" );
                                            }else {
//                                                readyWeight1 += Double.parseDouble( w2 );
                                            }
                                            totalWeight.setText( String.valueOf( totalWeight1 ) );
                                            System.out.println("====" + totalWeight1);
                                        }
                                    }
                                }
                            }
                        }
                        daim_listAdaptor = new DaimInfoAdapter( DaimInfo.this, daim_list );
                        recyclerView.setAdapter( daim_listAdaptor );
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            } );
        }else{
            System.out.println("-------");
        }

    }

    public class DaimInfoAdapter extends RecyclerView.Adapter<DaimInfoAdapter.ViewHolder> {
        Context context;
        List<DaimProcess> daim_list;

        public DaimInfoAdapter(Context context, List<DaimProcess> daim_list) {
            this.context = context;
            this.daim_list = daim_list;
        }

        @NonNull
        @Override
        public DaimInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.rc_daiminfo, parent, false );
            return new DaimInfoAdapter.ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull DaimInfoAdapter.ViewHolder holder, int position) {
            daimProcess1 = daim_list.get( position );
            holder.daimNo.setText( daimProcess1.getDaimNo() );
            holder.startDate.setText( daimProcess1.getStartDate() );
            holder.ruffWeight.setText( daimProcess1.getRuffWeight() );
            holder.readyWeight.setText( daimProcess1.getReadyWeight() );
            holder.sendTo.setText( daimProcess1.getReadyWeight() );

            if (daimProcess1.getStatus().equals( "Pending" )) {
                holder.status.setText( daimProcess1.getStatus() );
                holder.status.setTextColor( Color.parseColor( "#FFFF0000" ) );
            }else {
                holder.status.setText( daimProcess1.getStatus() );
                holder.status.setTextColor( Color.parseColor( "#349400" ) );
            }

            holder.rc_position.setOnClickListener( v->{
                Intent intent = new Intent(context, DaimPosition.class);
                intent.putExtra("DaimNo", daim_list.get( position ).getDaimNo());
                intent.putExtra("RuffWeight", daim_list.get( position ).getRuffWeight());
                intent.putExtra("Status", daim_list.get( position ).getStatus());
                intent.putExtra( "KapanNo", Kapan);
                intent.putExtra( "Role", Role );
                startActivity( intent );
            } );
        }
        @Override
        public int getItemCount () {
            return daim_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView startDate, ruffWeight, readyWeight, sendTo, status, daimNo;
            ConstraintLayout rc_position;

            public ViewHolder(View itemView) {
                super( itemView );
                rc_position = itemView.findViewById( R.id.rc_position );
                daimNo = itemView.findViewById( R.id.daimNo );
                startDate = itemView.findViewById( R.id.startDate );
                ruffWeight = itemView.findViewById( R.id.ruffWeight );
                readyWeight = itemView.findViewById( R.id.readyWeight );
                sendTo = itemView.findViewById( R.id.sendTo );
                status = itemView.findViewById( R.id.status );
            }
        }
    }
}