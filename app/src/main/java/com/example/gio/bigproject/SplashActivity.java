package com.example.gio.bigproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

public class SplashActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);  //Delay of 10 seconds
                } catch (Exception e) {
                    Log.d("Error Splash Screen", "run: ");
                } finally {

                    Intent i = new Intent(SplashActivity.this,
                            MapActivity_.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
