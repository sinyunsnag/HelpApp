package com.helpapplication.activities;

import com.helpapplication.R;
import com.helpapplication.utils.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPhoneNumberEdt;
    private EditText mCarNumberEdit;
    private PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());
        initView();
    }

    private void initView() {
        findViewById(R.id.start_drive_btn).setOnClickListener(this);
        findViewById(R.id.start_drive_btn_2).setOnClickListener(this);
        findViewById(R.id.setting_btn).setOnClickListener(this);
        mPhoneNumberEdt = (EditText) findViewById(R.id.number_edit);
        mPhoneNumberEdt.setText(mPreferenceManager.getReceiver());
        mCarNumberEdit = (EditText) findViewById(R.id.car_number_edit);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_drive_btn:
            case R.id.start_drive_btn_2:
            case R.id.setting_btn:
                if (!TextUtils.isEmpty(mPhoneNumberEdt.getText().toString()) && !TextUtils.isEmpty(mCarNumberEdit.getText().toString())) {
                    mPreferenceManager.setReceiver(mPhoneNumberEdt.getText().toString());
                    mPreferenceManager.setCarNumber(mCarNumberEdit.getText().toString());
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                } else {
                    Toast.makeText(this, "모든 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
