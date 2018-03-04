package com.larry.shugo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.SDKInitializer;
import com.larry.shugo.utils.ConfigUtil;

import cn.bmob.v3.Bmob;
import cn.sharesdk.framework.ShareSDK;

/**
 * 基类Activity
 */
public class BaseActivity extends AppCompatActivity{

    public Context context;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        SDKInitializer.initialize(context);
        Bmob.initialize(context,ConfigUtil.BMOB_APP_ID);

        //初始化分享SDK
        ShareSDK.initSDK(context);

    }

    /**
     * 开启进度弹窗
     * @param content
     */
    public void showProgressDialog(Context c,String content){
        if (progressDialog==null) {
            progressDialog = new ProgressDialog(c);
            progressDialog.setMessage(content);

        }

        progressDialog.show();
    }

    /**
     * 关闭进度弹窗
     */
    public void closeProgressDialog() {
        if (progressDialog!=null) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(context);
    }
}