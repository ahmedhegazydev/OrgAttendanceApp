package com.example.ahmed.orgattendanceapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ahmed.orgattendanceapp.fragments.FragmentSettings;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> fragments = null;
    ArrayList<String> pageTitles = null;


    public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments, ArrayList<String> pageTitles) {
        super(fragmentManager);
        this.fragments = fragments;
        this.pageTitles = pageTitles;


    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return fragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return FragmentSettings.newInstance(position, pageTitles.get(position));
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return this.pageTitles.get(position);
    }

}