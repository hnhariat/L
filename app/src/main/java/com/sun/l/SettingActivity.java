package com.sun.l;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sun.l.adapters.AdapterMakeGroup;
import com.sun.l.manager.FileManager;
import com.sun.l.models.DataFolder;
import com.sun.l.utils.LBitmapCache;
import com.sun.l.utils.PrefManager;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ctContainer;
    private TextView btnAdd;
    private Button btnSetBackground;
    private Button btnSortName;
    private Button btnSortTime;
    private Button btnSortCustom;
    private String mSortOrder;
    private String mGroupState;

    private Button btnSortDefault;
    private Button btnMakeFolder;
    private ScrollView scrollView;
    private LinearLayout container;
    private View folder;
    private Button btnSortSimple;
    private Intent mIntentResult;
    private RecyclerView gridFolder;
    private ArrayList<DataFolder> mListFolder;
    private AdapterMakeGroup adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();
    }

    @Override
    public void initData() {
        super.initData();
        mSortOrder = PrefManager.getInstance().getString(getApplicationContext(), LConst.PrefKey.sort);
        mGroupState = PrefManager.getInstance().getString(getApplicationContext(), LConst.PrefKey.group);
        mIntentResult = new Intent();
        mListFolder = new ArrayList<DataFolder>();
        mListFolder.add(new DataFolder("추가", DataFolder.TYPE_NEW, -1));
        mListFolder.add(new DataFolder("추가", DataFolder.TYPE_NEW, -1));
        mListFolder.add(new DataFolder("추가", DataFolder.TYPE_NEW, -1));
        mListFolder.add(new DataFolder("추가", DataFolder.TYPE_NEW, -1));
        //TODO : add all folders
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void initView() {
        super.initView();
        ctContainer = (LinearLayout) findViewById(R.id.container);
        ctContainer.setLayoutTransition(new LayoutTransition());

        btnAdd = (TextView) findViewById(R.id.btn_add);
        btnSetBackground = (Button) findViewById(R.id.btn_setting_background);

        btnSortName = (Button) findViewById(R.id.btn1);
        btnSortTime = (Button) findViewById(R.id.btn2);
        btnSortCustom = (Button) findViewById(R.id.btn3);
        btnSortDefault = (Button) findViewById(R.id.btn6);

        btnSortSimple = (Button) findViewById(R.id.btn7);
        btnMakeFolder = (Button) findViewById(R.id.btn_make_folder);
        scrollView = (ScrollView) findViewById(R.id.root);

        folder = View.inflate(getApplicationContext(), R.layout.item_make_group, null);
        gridFolder = (RecyclerView) folder.findViewById(R.id.rcv);
        GridLayoutManager gm = new GridLayoutManager(getApplicationContext(), 4);
        gridFolder.setLayoutManager(gm);

        btnMakeFolder.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LUtils.getScreenHeight(getApplicationContext()) - btnMakeFolder.getHeight() - LUtils.getStatusBarHeight(getApplicationContext()));
                folder.setLayoutParams(params);
            }
        });

        container = (LinearLayout) findViewById(R.id.container);
    }

    @Override
    public void initControl() {
        super.initControl();
        btnAdd.setOnClickListener(this);
        btnSetBackground.setOnClickListener(this);

        btnSortName.setOnClickListener(this);
        btnSortTime.setOnClickListener(this);
        btnSortCustom.setOnClickListener(this);
        btnSortDefault.setOnClickListener(this);
        btnSortSimple.setOnClickListener(this);

        btnMakeFolder.setOnClickListener(this);

        adapter = new AdapterMakeGroup();
        adapter.setList(mListFolder);
        gridFolder.setAdapter(adapter);
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
        } else if (id == R.id.btn1) {
            setSortOrder(LConst.PrefValue.SORT_NAME);
        } else if (id == R.id.btn2) {
            setSortOrder(LConst.PrefValue.SORT_TIME);
        } else if (id == R.id.btn3) {
            setSortOrder(LConst.PrefValue.SORT_CUSTOM);
        } else if (id == R.id.btn6) {
            setSortOrder(LConst.PrefValue.SORT_DEFAULT);
        } else if (id == R.id.btn_make_folder) {
            container.removeView(folder);
            container.addView(folder);
            ValueAnimator animator = ValueAnimator.ofInt(0, btnMakeFolder.getTop() + getSupportActionBar().getHeight());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    scrollView.scrollTo(0, value);
                }
            });
            animator.setDuration(350);
            animator.start();
        } else if (id == R.id.btn7) {
            setSimpleGroup(LConst.PrefValue.GROUP_NAME_SIMILAR);
        }
    }

    private void setSimpleGroup(String value) {
        PrefManager.getInstance().putString(getApplicationContext(), LConst.PrefKey.group, value);
        setResult(RESULT_OK);
        if (!mGroupState.equals(value)) {
        } else {
            // TODO : 모든 설정들의 변함을 감지해야함
//            setResult(RESULT_CANCELED);
        }
    }

    private void setSortOrder(String value) {
        PrefManager.getInstance().putString(getApplicationContext(), LConst.PrefKey.sort, value);
        setResult(RESULT_OK);
        if (!mSortOrder.equals(value)) {
        } else {
            // TODO : 모든 설정들의 변함을 감지해야함
//            setResult(RESULT_CANCELED);
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
