package com.example.natharam.searchtoilet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Thread th = new Thread() {
            @Override
            public void run() {

                try {

                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent obj = new Intent(SendActivity.this, MapsActivity.class);
                    startActivity(obj);
                }


            }

        } ;
        th.start();
    }
    @Override
    protected void onPause(){

        super.onPause();
        finish();
    }

}
