package com.larry.shugo.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.larry.shugo.R;
import com.larry.shugo.model.biz.ActivityManager;
import com.larry.shugo.utils.GeneralUtil;

public class SelectPictureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        ActivityManager.getInstance().pushOneActivity(this);
        getImage();
    }

    private void getImage() {
        if(GeneralUtil.isSDCard()){
            Toast.makeText(context,"无外部存储",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
