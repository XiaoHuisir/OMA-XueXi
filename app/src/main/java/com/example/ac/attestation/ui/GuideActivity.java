package com.example.ac.attestation.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ac.attestation.R;
import com.example.ac.attestation.tool.SPUtils;

public class GuideActivity extends AppCompatActivity {
    private static final int TYPE = 2021;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE:
                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
                   SPUtils.putParam(GuideActivity.this, "isGuide", true);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        handler.sendEmptyMessageDelayed(TYPE, 2000);
    }
}