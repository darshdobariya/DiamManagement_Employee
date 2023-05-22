package com.example.mobileauthentication.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mobileauthentication.work.Salary.Salary;
import com.example.mobileauthentication.work.Work.Work;

public class WorkAdapter extends FragmentPagerAdapter {

    public WorkAdapter(
            @NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new Work();
        else if (position == 1)
            fragment = new Salary();

        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Work";
        else if (position == 1)
            title = "Salary";
        return title;
    }
}
