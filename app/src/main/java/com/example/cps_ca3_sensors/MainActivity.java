package com.example.cps_ca3_sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Real Dim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGravity(View view) {
        Intent intent = new Intent(this, Gravity.class);
        startActivity(intent);
    }

    public void startGyroscope(View view) {
        Intent intent = new Intent(this, Gyroscope.class);
        startActivity(intent);
    }
}

