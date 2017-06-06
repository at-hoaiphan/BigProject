package com.example.gio.bigproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gio.bigproject.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

/**
 * Copyright by Gio.
 * Created on 4/28/2017.
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {
    public static final String NEED_RELOAD = "needReload";
    private static final String MAP_VIEW_TYPE_TRAFFIC = "Traffic";
    @ViewById(R.id.radioGroupMode)
    RadioGroup mRdGroupMode;
    @ViewById(R.id.radioGroupType)
    RadioGroup mRdGroupType;

//    @Pref
//    SettingsInterface_ mSettingsInterface;

//    @AfterViews
//    void afterViews() {
//        mRdGroupMode.check(mSettingsInterface.checkedModeType().get());
//        mRdGroupType.check(mSettingsInterface.checkedViewType().get());
//    }

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
        int checkedRbModeId = mRdGroupMode.getCheckedRadioButtonId();
        RadioButton rbCheckedMode = (RadioButton) findViewById(checkedRbModeId);

        int checkedRbTypeId = mRdGroupType.getCheckedRadioButtonId();
        RadioButton rbCheckedType = (RadioButton) findViewById(checkedRbTypeId);
        int type;
        if (Objects.equals(rbCheckedType.getText().toString(), MAP_VIEW_TYPE_TRAFFIC)) {
            type = 1;
        } else {
            type = 2;
        }
//        mSettingsInterface.edit()
//                .checkedModeType()
//                .put(mRdGroupMode.getCheckedRadioButtonId())
//                .mode()
//                .put(rbCheckedMode.getText().toString())
//                .type()
//                .put(type)
//                .checkedViewType()
//                .put(mRdGroupType.getCheckedRadioButtonId())
//                .apply();
        Toast.makeText(this, "Settings saved!", Toast.LENGTH_LONG).show();
    }

    private void reloadMap() {
        Intent data = new Intent();
        data.putExtra(NEED_RELOAD, true);
        this.setResult(RESULT_OK, data);
        finish();
    }
}


