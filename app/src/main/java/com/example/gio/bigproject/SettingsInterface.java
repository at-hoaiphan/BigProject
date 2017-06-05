package com.example.gio.bigproject;

import com.example.gio.bigproject.activities.MapActivity;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Copyright by Gio.
 * Created on 4/28/2017.
 */

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface SettingsInterface {

    @DefaultInt(R.id.rbWalking)
    int checkedModeType();
    @DefaultInt(R.id.rbTrafficType)
    int checkedViewType();

    @DefaultString(MapActivity.WALKING)
    String mode();
    @DefaultInt(1)
    int type();
}
