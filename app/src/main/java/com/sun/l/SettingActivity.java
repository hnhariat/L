package com.sun.l;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun.l.manager.FileManager;
import com.sun.l.utils.LBitmapCache;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ctContainer;
    private TextView btnAdd;
    private Button btnSetBackground;

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
        btnSetBackground = (Button) findViewById(R.id.btn_setting_background);
    }

    @Override
    public void initControl() {
        super.initControl();
        btnAdd.setOnClickListener(this);
        btnSetBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            View child = View.inflate(this, R.layout.item_category, null);
            ((TextView) child.findViewById(R.id.txt_label)).setText("항목");
            ctContainer.addView(child);
        } else if (id == R.id.btn_setting_background) {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, LConst.Request.Picture);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case LConst.Request.Picture:
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                FileManager.copyTo(filePath, FileManager.getRootPath() + FileManager.FILE_BACKGROUND);
                FileManager.saveBitmapCache(getApplicationContext(), filePath);
                Bitmap bmp = LBitmapCache.getInstance(getApplicationContext()).get(LConst.Key.background);
                Drawable drawable = new BitmapDrawable(getResources(), bmp);

                findViewById(R.id.root).setBackgroundDrawable(drawable);
                setResult(RESULT_OK);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
