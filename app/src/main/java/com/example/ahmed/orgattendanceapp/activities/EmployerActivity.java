package com.example.ahmed.orgattendanceapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ahmed.orgattendanceapp.LoginActivity;
import com.example.ahmed.orgattendanceapp.R;
import com.example.ahmed.orgattendanceapp.adapters.MyPagerAdapter;
import com.example.ahmed.orgattendanceapp.fragments.FragmentManage;
import com.example.ahmed.orgattendanceapp.fragments.FragmentReports;
import com.example.ahmed.orgattendanceapp.fragments.FragmentSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployerActivity extends AppCompatActivity {

    Context context = null;
    FragmentPagerAdapter adapterViewPager = null;
    @BindView(R.id.viewPagerEmployer)
    ViewPager viewPager;
    ArrayList<Fragment> listFragments = null;
    ArrayList<String> listPageTitles = null;
    Resources resources = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        initGlobalVars();
        initViewPager();

    }

    private void initGlobalVars() {
        context = this;
        resources = getResources();
        ButterKnife.bind(this);


    }

    private void initViewPager() {

        // add fragments as pages
        listFragments = new ArrayList<>();
        listFragments.add(new FragmentManage());
        listFragments.add(new FragmentReports());
        listFragments.add(new FragmentSettings());


        //add the pages titles
        listPageTitles = new ArrayList<String>(Arrays.asList(resources.getStringArray(R.array.array_employer)));


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), listFragments, listPageTitles);
        viewPager.setAdapter(adapterViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_employer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutEmployer:
                signOut();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

}
