package com.example.owner.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ZoneActivity extends AppCompatActivity {

    TextView park, zone, currentNo, maxNo, deltaNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        park = (TextView)findViewById(R.id.textView);
        zone = (TextView)findViewById(R.id.textView2);
        maxNo = (TextView)findViewById(R.id.textView3);
        currentNo = (TextView)findViewById(R.id.textView4);

        deltaNo = (TextView)findViewById(R.id.textView5);
        Intent intent = getIntent();
        String[] values = intent.getStringArrayExtra("values");
        park.setText("ParkingLot: " + values[0]);
        zone.setText("ZONE: " + values[1]);
        maxNo.setText("maxNo:" + values[2]);
        currentNo.setText("currentNo: " + values[3]);
        deltaNo.setText("deltaNo: " + values[4]);
    }
}
