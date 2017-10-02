package com.example.mojiehua93.myclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ClockView mClockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClockView = (ClockView)findViewById(R.id.clockView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mClockView.stopClock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mClockView.startClock();
    }
}
