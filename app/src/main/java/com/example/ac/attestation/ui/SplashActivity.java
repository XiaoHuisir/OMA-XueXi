package com.example.ac.attestation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ac.attestation.R;
import com.example.ac.attestation.tool.SPUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void initData() {
        Boolean isGuide = (Boolean) SPUtils.getParam(this, "isGuide", false);
        Intent intent = new Intent();

        if (isGuide) {
            intent.setClass(this, MainActivity.class);

        } else {
            intent.setClass(this, GuideActivity.class);

        }
        startActivity(intent);
        finish();
    }
}