package com.example.mobileauthentication.work.Work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Work extends Fragment {

    String userRole, a, b;
    RecyclerView recyclerView;
    WorkAdapter daim_listAdaptor;
    List<DaimProcess> daim_list;
    DatabaseReference myRef, myRef2;
    DaimProcess daimProcess;
    int totaldaim = 0;
    int readyDaimond = 0;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_work,container,false);
    }

    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        recyclerView = view.findViewById( R.id.rcWork );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( view.getContext() ) );
        recyclerView.getLayoutManager().setMeasurementCacheEnabled( false );

        daim_list = new ArrayList<>();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference( "Maruti Daim" ).child( "Kapan" );
        mAuth = FirebaseAuth.getInstance();

        userRole();
        getdata();
    }

    public void userRole(){
        myRef2 = FirebaseDatabase.getInstance().getReference("Maruti Daim").child( "User" ).child( "Pass" ).child( mAuth.getUid() );
        myRef2.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userRole = snapshot.child( "Role" ).getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        } );
    }

    public void getdata() {
        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                daim_list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studentsnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot : studentsnapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    if (snapshot2.getKey().equals( userRole ) )
                                    {
                                        totaldaim += 1;
                                        if (snapshot2.child( "Status" ).getValue().toString().equals( "Pass" ))
                                        {
                                            readyDaimond += 1;
                                            Toast.makeText( getContext(), "Hip Hip Hurrah..", Toast.LENGTH_SHORT ).show();
                                        }
                                    }
                                }
                            }daim_listAdaptor.notifyDataSetChanged();
                        }
                        b = String.valueOf( readyDaimond );
                        a = String.valueOf( totaldaim );

                        daimProcess = new DaimProcess(studentsnapshot.getKey(), b, a);
                        daim_list.add( daimProcess );
                        readyDaimond = 0;totaldaim = 0;
                    }
                    daim_listAdaptor.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );

        daim_listAdaptor = new WorkAdapter( getActivity(), daim_list );
        recyclerView.setAdapter( daim_listAdaptor );
    }

    public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {
        Context context;
        List<DaimProcess> daim_list;

        public WorkAdapter(Context context, List<DaimProcess> daim_list) {
            this.context = context;
            this.daim_list = daim_list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.rc_work, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            daimProcess = daim_list.get( position );
            holder.kapan.setText( daimProcess.getKapan() );
            holder.ready.setText( daimProcess.getReady() );
            holder.total.setText( daimProcess.getTotaldaim() );

            if (daimProcess.getTotaldaim().equals( daimProcess.getReady() )){
                holder.status.setText( "Pass" );
                holder.status.setTextColor( Color.parseColor("#FF027314"));
            }else {
                holder.status.setText( "Pending..." ); holder.status.setTextColor( Color.parseColor("#FFFF0000"));
            }
            holder.card1.setOnClickListener( v ->{
                Intent intent = new Intent(v.getContext(), DaimInfo.class);
                intent.putExtra( "Kapan", holder.kapan.getText() );
                intent.putExtra( "Role", userRole );
                startActivity( intent );
            } );

        }
        @Override
        public int getItemCount () {
            return daim_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView kapan, ready, total, status;
            CardView card1;

            public ViewHolder(View itemView) {
                super( itemView );
                card1 = itemView.findViewById( R.id.card1 );
                kapan = itemView.findViewById( R.id.kapan );
                ready = itemView.findViewById( R.id.ready );
                total = itemView.findViewById( R.id.total );
                status = itemView.findViewById( R.id.status );
            }
        }
    }
}

