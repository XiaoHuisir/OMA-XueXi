package com.example.ac.attestation.tool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ac.attestation.R;


public class DialogUtils extends AlertDialog {

    private static final String TAG = DialogUtils.class.getSimpleName();
    private String message;
    private Context mContext;
    private AnimationDrawable drawable;
    private TextView textMsg;
    private RelativeLayout loadingParent;
    private int width;
    private int height;
    private int textColor;
    private int textSize;
    private boolean isShow;
    private int backgroundResource;


    public DialogUtils(Context context) {
        super(context);
        mContext = context;
    }

    public DialogUtils(Context context, String message) {
        super(context);
        mContext = context;
        this.message = message;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        } catch (Exception ignored) {
        }
        setContentView(R.layout.oauth_loading_dialog);

        textMsg = (TextView) findViewById(R.id.oauth_loading_dialog_txt);
        loadingParent = (RelativeLayout) findViewById(R.id.loading_parent);
        ImageView imageView = (ImageView) findViewById(R.id.oauth_loading_dialog_img);

        drawable = (AnimationDrawable) imageView.getDrawable();

        textMsg.setText(message);
        //执行配置
        init();
    }

    private void init() {
        //宽高
        if (width != 0 && height != 0) {
            loadingParent.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        }
        //字体大小
        if (textSize != 0) {
            textMsg.setTextSize(textSize);
        }
        //字体颜色
        if (textColor != 0) {
            textMsg.setTextColor(textColor);
        }
        if (!isShow) {
            textMsg.setVisibility(View.GONE);
        }
        if (backgroundResource != 0) {
            loadingParent.setBackgroundResource(backgroundResource);
        }
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        this.textSize = size;
    }

    /**
     * 是否显示文字
     *
     * @param isShow
     */
    public void isShowText(boolean isShow) {
        this.isShow = isShow;
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        this.textColor = color;
    }

    /**
     * 设置宽高
     *
     * @param width
     * @param height
     */
    public void setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 设置backgroundResource
     *
     * @param backgroundResource
     */
    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }


    @Override
    protected void onStart() {
        drawable.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        drawable.stop();
        super.onStop();
    }
}
