package com.example.ahmed.orgattendanceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ahmed.orgattendanceapp.LoginActivity;
import com.example.ahmed.orgattendanceapp.R;
import com.example.ahmed.orgattendanceapp.fragments.DemoFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class EmployerActivity extends AppCompatActivity {

    private static final String KEY_DEMO = "demo";
    Demo demo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        demo = getDemo();
        init();


    }

    private Demo getDemo() {
        return Demo.valueOf(getIntent().getStringExtra(KEY_DEMO));
    }

    private void init() {


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_wrapper, new MapEmployer());
//        fragmentTransaction.commit();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(demo.titleResId);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
        //tab.addView(LayoutInflater.from(this).inflate(demo.layoutResId, tab, false));

        ViewPager viewPager = findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        demo.setup(viewPagerTab);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (int i = 0; i < 3; i++) {
            pages.add(FragmentPagerItem.of(i + "", DemoFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
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
