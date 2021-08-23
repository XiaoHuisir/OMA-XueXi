package com.example.ac.attestation.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ac.attestation.LoginSuccessActivity;
import com.example.ac.attestation.R;
import com.example.ac.attestation.tool.DialogUtils;
import com.example.ac.attestation.tool.UIHelper;
import com.hank.oma.SmartCard;
import com.hank.oma.core.EnumReaderType;
import com.hank.oma.entity.CardResult;
import com.hank.oma.utils.LogUtil;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

private Context context;
    private EditText ed_pwd_SIM;
    private LinearLayout line_login;
    private TextView tv_login;
    private static final int  TYPE_LOGIN=2022;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TYPE_LOGIN:
                    dialogUtils.dismiss();
                    startActivity(new Intent(context, LoginSuccessActivity.class));

                    break;
            }
        }
    };
    private DialogUtils dialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        initView();
        dialogUtils = new DialogUtils(this, "加载中...");
        dialogUtils.setWidthHeight(300, 300);
        dialogUtils.setTextColor(Color.RED);
        dialogUtils.setTextSize(16);
        dialogUtils.isShowText(true);
    }

    private void initView() {
        //输入密码框
        ed_pwd_SIM = findViewById(R.id.ed_pwd_SIM);
        //登录键
        line_login = findViewById(R.id.line_login);
        tv_login = findViewById(R.id.tv_login);
        line_login.setOnClickListener(this);
        ed_pwd_SIM.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.line_login://登录
                //隐藏软键盘
                View views = getWindow().peekDecorView();
                if (views != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                String trim = ed_pwd_SIM.getText().toString().trim();
                if (!isFastDoubleClick()) {
                    if (trim.equals("")) {
                        UIHelper.ToastMessage(context, context.getString(R.string.import_pwd));
                        return;
                    }
                    dialogUtils.show();
                    SmartCard.getInstance().setmReaderType(EnumReaderType.READER_TYPE_SIM);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final CardResult cardResult = SmartCard.getInstance().execute("00A4040010D1560001010001600000000100000000", "9000");
                            if (cardResult.getStatus() == 0) {
                                LogUtil.i(cardResult.getRapdu());
                                LogUtil.i(cardResult.getSw());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplication(), "开通道成功" + cardResult.getMessage(), Toast.LENGTH_SHORT).show();
//                                        openchannel.setText("Message:" + cardResult.getMessage() + "\nRapdu:" + cardResult.getRapdu() + "\nSw:" + cardResult.getSw());

                                    }
                                });

                                transceive();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplication(), "开通道失败" + cardResult.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                LogUtil.i(cardResult.getMessage());
                                SmartCard.getInstance().closeChannel();
                                SmartCard.getInstance().closeService();
                            }
                        }
                    }).start();
                }
                break;
            case R.id.ed_pwd_SIM:
                initData(ed_pwd_SIM);
                break;
        }
    }
    public void transceive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final CardResult cardResult = SmartCard.getInstance().execute("80502400089FBD88CBBABC07DC00");
                if (cardResult.getStatus() == 0) {
                    LogUtil.i(cardResult.getRapdu());
                    LogUtil.i(cardResult.getSw());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessageDelayed(TYPE_LOGIN,1000);
                            Toast.makeText(getApplication(), "登录执行成功" + cardResult.getMessage(), Toast.LENGTH_SHORT).show();
//                            transceiveText.setText("Message:" + cardResult.getMessage() + "\nRapdu:" + cardResult.getRapdu() + "\nSw:" + cardResult.getSw());

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplication(), "登录执行失败" + cardResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    LogUtil.i(cardResult.getMessage());
                    SmartCard.getInstance().closeChannel();
                    SmartCard.getInstance().closeService();
                }
            }
        }).start();
    }
    private static long lastClickTime;
    /**
     * 防止 button 多次点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = SystemClock.uptimeMillis(); // 此方法仅用于Android
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    /**
     * 登录按钮状态变化设置
     *
     * @param editText
     */
    private void initData(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String type = s.toString();
                Log.d("ceshi", "onTextChanged: " + type);
                if (type == null || type.equals("")) {
                    Resources resources = getBaseContext().getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.button_bg);
                    line_login.setBackgroundDrawable(drawable);
                    tv_login.setTextColor(getResources().getColor(R.color.color_text_not));
                } else {
                    Resources resources = getBaseContext().getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.button_bg_ok);
                    line_login.setBackgroundDrawable(drawable);
                    tv_login.setTextColor(getResources().getColor(R.color.white));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userpwdSIM = ed_pwd_SIM.getText().toString().trim();

            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            //点击空白位置 隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }

        return super.onTouchEvent(event);
    }
}