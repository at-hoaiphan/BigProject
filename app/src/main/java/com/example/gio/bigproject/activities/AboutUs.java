package com.example.gio.bigproject.activities;

import android.support.v7.app.AppCompatActivity;

import com.example.gio.bigproject.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Copyright by Gio.
 * Created on 5/16/2017.
 */

@EActivity(R.layout.about_us)
public class AboutUs extends AppCompatActivity {
    @Click(R.id.fabBack)
    void clickBack() {
        finish();
    }
}