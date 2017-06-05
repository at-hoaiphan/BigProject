package com.example.gio.bigproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gio.bigproject.R;
import com.example.gio.bigproject.SettingsInterface_;

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
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {
    private static final String MAP_VIEW_TYPE_TRAFFIC = "Traffic";
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
        if (Objects.equals(rbCheckedType.getText().toString(), MAP_VIEW_TYPE_TRAFFIC)) {
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
    }

    private void reloadMap() {
        Intent data = new Intent();
        data.putExtra(MapActivity.NEED_RELOAD, true);
        this.setResult(RESULT_OK, data);
        finish();
    }
}


