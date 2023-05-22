package com.example.mobileauthentication.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileauthentication.Adapter.WorkAdapter;
import com.example.mobileauthentication.R;
import com.google.android.material.tabs.TabLayout;

public class WorkFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    WorkAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_work,container,false);

    }

    @Override

    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);

        viewPagerAdapter = new WorkAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // It is used to join TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);

    }
}
