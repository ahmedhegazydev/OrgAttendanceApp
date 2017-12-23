package com.example.ahmed.orgattendanceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ahmed.orgattendanceapp.LoginActivity;
import com.example.ahmed.orgattendanceapp.R;
import com.example.ahmed.orgattendanceapp.fragments.MapEmployer;
import com.google.firebase.auth.FirebaseAuth;

public class EmployerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_wrapper, new MapEmployer());
        fragmentTransaction.commit();


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
