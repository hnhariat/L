package com.sun.l;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ctContainer;
    private TextView btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void initView() {
        super.initView();
        ctContainer = (LinearLayout) findViewById(R.id.container);
        ctContainer.setLayoutTransition(new LayoutTransition());

        btnAdd = (TextView) findViewById(R.id.btn_add);
    }

    @Override
    public void initControl() {
        super.initControl();
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            View child = View.inflate(this, R.layout.item_category, null);
            ((TextView) child.findViewById(R.id.txt_label)).setText("항목");
            ctContainer.addView(child);
        }
    }
}
