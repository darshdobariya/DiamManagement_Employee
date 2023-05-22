package com.example.mobileauthentication.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileauthentication.MobileSignUp;
import com.example.mobileauthentication.R;
import com.example.mobileauthentication.Setting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment  extends Fragment {

    ImageButton logOut,addBtn;
    FirebaseAuth mAuth;
    ImageView setting;
    int images[] = {R.drawable.p1, R.drawable.p2, R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6};
    int currentPageCounter = 0;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home,container,false);

    }

    @Override

    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setting = view.findViewById( R.id.setting );

        mAuth = FirebaseAuth.getInstance();

        setting.setOnClickListener( v->{
            startActivity( new Intent( getContext(), Setting.class ) );
        } );

        // Image slider
        viewPager = view.findViewById( R.id.view_pager );
        viewPager.setAdapter( new SliderAdapter( images, getContext() ) );

        // auto change image
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPageCounter == images.length) {
                    currentPageCounter = 0;
                }
                viewPager.setCurrentItem( currentPageCounter++, true );
            }
        };

        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            @Override
            public void run() {
                handler.post( update );
            }
        }, 5000, 5000 );
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity( new Intent( getContext(), MobileSignUp.class ) );
        }
    }

    public class SliderAdapter extends PagerAdapter {

        int[] images;
        LayoutInflater layoutInflater;
        Context context;

        //alt + ins to create cons


        public SliderAdapter(int[] images, Context context) {
            this.images = images;
            this.layoutInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View myImageLayout = layoutInflater.inflate( R.layout.image_slide,container, false);
            ImageView imageview = myImageLayout.findViewById(R.id.imageview);
            TextView slideno = myImageLayout.findViewById( R.id.slideNo );
            TextView slidetotal = myImageLayout.findViewById( R.id.slideTotal );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageview.setImageDrawable(context.getDrawable(images[position]));
                slideno.setText( String.valueOf( position+1 ) );
                slidetotal.setText( String.valueOf( images.length ) );
            }else {
                imageview.setImageDrawable(context.getResources().getDrawable(images[position]));
            }

            container.addView(myImageLayout);

            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

            return view.equals(object);
        }
    }

}
