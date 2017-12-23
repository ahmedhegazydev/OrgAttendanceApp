package com.example.ahmed.orgattendanceapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ahmed.orgattendanceapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class EmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);


        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this, "Null user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



}
