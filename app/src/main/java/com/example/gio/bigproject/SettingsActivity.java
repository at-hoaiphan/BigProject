package com.example.gio.bigproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Objects;

/**
 * Copyright by Gio.
 * Created on 4/28/2017.
 */
@EActivity(R.layout.settings)
public class SettingsActivity extends AppCompatActivity {

    @ViewById(R.id.swEnableLocation)
    Switch swEnableLocation;

    @ViewById(R.id.radioGroupMode)
    RadioGroup rdGroupMode;
    @ViewById(R.id.rbWalking)
    RadioButton rbWalking;
    @ViewById(R.id.rbBicycle)
    RadioButton rbBicycle;
    @ViewById(R.id.rbMotor)
    RadioButton rbMotor;
    @ViewById(R.id.rbCar)
    RadioButton rbCar;

    @ViewById(R.id.radioGroupType)
    RadioGroup rdGroupType;
    @ViewById(R.id.btnOk)
    Button btnOk;
    @ViewById(R.id.btnCancel)
    Button btnCancel;

    @Pref
    SettingsInterface_ settingsInterface;

    @AfterViews
    void afterViews() {
        rdGroupMode.check(settingsInterface.checkedModeType().get());
        rdGroupType.check(settingsInterface.checkedViewType().get());
        Log.d("Check", "afterViews: " +  settingsInterface.checkedModeType().toString());
    }

    @Click(R.id.btnCancel)
    void clickBtnCancel() {
        finish();
    }

    @Click(R.id.btnOk)
    void clickBtnOk() {
        saveSettings();
        reloadMap();
    }

    // Called when user press Save button
    private void saveSettings() {
        int checkedRbModeId = rdGroupMode.getCheckedRadioButtonId();
        RadioButton rbCheckedMode = (RadioButton) findViewById(checkedRbModeId);

        int checkedRbTypeId = rdGroupType.getCheckedRadioButtonId();
        RadioButton rbCheckedType = (RadioButton) findViewById(checkedRbTypeId);
        int type;
        if (Objects.equals(rbCheckedType.getText().toString(), "Traffic")) {
            type = 1;
        } else {
            type = 2;
        }
        settingsInterface.edit()
                .checkedModeType()
                .put(rdGroupMode.getCheckedRadioButtonId())
                .mode()
                .put(rbCheckedMode.getText().toString())
                .type()
                .put(type)
                .checkedViewType()
                .put(rdGroupType.getCheckedRadioButtonId())
                .apply();
        Toast.makeText(this, "Settings saved!", Toast.LENGTH_LONG).show();
        Log.d("Check", "saveSettings: " + rbCheckedMode.getText());
    }

    private void reloadMap() {
        Intent data = new Intent();
        data.putExtra("needReload", true);
        this.setResult(RESULT_OK, data);
        finish();
    }
}


