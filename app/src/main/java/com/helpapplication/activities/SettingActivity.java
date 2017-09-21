package com.helpapplication.activities;

import com.helpapplication.R;
import com.helpapplication.utils.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

public class SettingActivity extends AppCompatActivity {

    private PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());
        initView();
    }

    private void initView() {
        final EditText phoneNumberEdit = (EditText) findViewById(R.id.setting_number_edit);
        phoneNumberEdit.setText(mPreferenceManager.getCaller());

        findViewById(R.id.setting_complete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, MapActivity.class));
                mPreferenceManager.setCaller(phoneNumberEdit.getText().toString());
            }
        });

        radioButtonSettings();
    }

    private void radioButtonSettings() {

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.service_time_one_minute_rdb:
                        mPreferenceManager.setIsServicePeriodOneMinute(isChecked);
                        break;
                    case R.id.service_time_three_minute_rdb:
                        mPreferenceManager.setIsServicePeriodThreeMinute(isChecked);
                        break;
                    case R.id.service_time_five_minute_rdb:
                        mPreferenceManager.setIsServicePeriodFiveMinute(isChecked);
                        break;
                    case R.id.service_uses_rdb:
                        mPreferenceManager.setIsServiceUses(isChecked);
                        break;
                    case R.id.service_unuses_rdb:
                        mPreferenceManager.setIsServiceUnUses(isChecked);
                        break;
                }
            }
        };

        ((RadioButton) findViewById(R.id.service_time_one_minute_rdb)).setChecked(mPreferenceManager.isServicePeriodOneMinute());
        ((RadioButton) findViewById(R.id.service_time_three_minute_rdb)).setChecked(mPreferenceManager.isServicePeriodThreeMinute());
        ((RadioButton) findViewById(R.id.service_time_five_minute_rdb)).setChecked(mPreferenceManager.isServicePeriodFiveMinute());
        ((RadioButton) findViewById(R.id.service_uses_rdb)).setChecked(mPreferenceManager.isServiceUses());
        ((RadioButton) findViewById(R.id.service_unuses_rdb)).setChecked(mPreferenceManager.isServiceUnUses());

        ((RadioButton) findViewById(R.id.service_time_one_minute_rdb)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) findViewById(R.id.service_time_three_minute_rdb)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) findViewById(R.id.service_time_five_minute_rdb)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) findViewById(R.id.service_uses_rdb)).setOnCheckedChangeListener(onCheckedChangeListener);
        ((RadioButton) findViewById(R.id.service_unuses_rdb)).setOnCheckedChangeListener(onCheckedChangeListener);
    }

}
