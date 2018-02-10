package com.yang.runbang.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.yang.runbang.R;
import com.yang.runbang.adapter.PublishGridAdapter;
import com.yang.runbang.model.bean.Dynamic;
import com.yang.runbang.model.bean.Friend;
import com.yang.runbang.model.bean.Timeline;
import com.yang.runbang.model.bean.User;
import com.yang.runbang.model.biz.ActivityManager;
import com.yang.runbang.utils.FileUtil;
import com.yang.runbang.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PublishDynamicActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {


    private final static int REQUEST_CODE_CAMERA=0X001; //拍照的requestcode

    private static final int GET_PHOTO_REQUEST = 0X11;

    private static final int UPLOAD_PICTURE_SUCCESS = 0X12;


    private static final int Push_Dynamic_Success = 0x13;

    private static final int Save_Dynamic_To_MYTimeline = 0x14;

    private static final int Push_Dynamic_Failure = 0x15;



    private EditText contentEdt; //动态内容
    private GridView mGridView;
    private List<String> mSelectedPicture = new ArrayList<>(); //选中图片
    private PublishGridAdapter adapter;

    private PopupWindow popupWindow;
    private RelativeLayout popupRelative;
    private TextView photoText,cameraText,cancelText;

    private User user; //当前用户

    private Timeline myTimeline; //当前用户的时间线

    private Dynamic dynamic = null; //动态

    private String content = null; //内容

    private String theme = null;//主题

    private List<String> pictureList = new ArrayList<>(); //图片url

    //发布动态完成标志
    private boolean isPushFinish = true;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case UPLOAD_PICTURE_SUCCESS: //上传图片文件成功

                    Bundle bundle = msg.getData();
                    //获取图片url集合
                    pictureList = bundle.getStringArrayList("urls");
                    //发布动态
                    publishDynamic();
                    break;
                case Push_Dynamic_Success: //推送动态成功

                    isPushFinish = true;
                    //关闭提示
                    closeProgressDialog();
                    //展示dialog
                    showDialog();

                    break;
                case Push_Dynamic_Failure://发布动态失败
                    isPushFinish = true;
                    Toast.makeText(PublishDynamicActivity.this, "发布动态失败,请稍后重试", Toast.LENGTH_SHORT).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_dynamic);
        ActivityManager.getInstance().pushOneActivity(this);
        user = BmobUser.getCurrentUser(context,User.class);
        //查询个人时间线
        queryMyTimeline();

        initComponent();
        initPopupWindow();
        initGridView();

    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_publish);
        toolbar.setTitle("写动态");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(this);

        contentEdt = (EditText) findViewById(R.id.publish_content);
        mGridView = (GridView) findViewById(R.id.publish_gridview);

    }



    /**
     * 上传图片
     */
    private void uploadPicture(){

        final  String[] filePaths = new String[mSelectedPicture.size()];

        for (int i =0;i<mSelectedPicture.size();i++) {
            filePaths[i] = mSelectedPicture.get(i);
        }

        BmobFile.uploadBatch(context, filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {

                if (list1.size() == filePaths.length) { //全部上传完成

                    Message msg = new Message();
                    msg.what = UPLOAD_PICTURE_SUCCESS;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("urls", (ArrayList) list1);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {


            }

            @Override
            public void onError(int i, String s) {

                handler.sendEmptyMessage(Push_Dynamic_Failure);
            }
        });
    }

    /**
     * 初始化gridview
     */
    private void initGridView(){

        adapter = new PublishGridAdapter(context,mSelectedPicture);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position == mSelectedPicture.size()) { //加
                    popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_publish_dynamic, null), Gravity.BOTTOM, 0, 0);

                } else {

                }
            }
        });


    }


    /**
     * 检查输入
     * @return
     */
    private boolean checkInput(){

        if (contentEdt.getText().length()>0 || mSelectedPicture.size()>0) {

            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        popupWindow.dismiss();
        switch (requestCode) {
            case GET_PHOTO_REQUEST: //获取图片返回

                if ( resultCode == AlbumActivity.RESULT_CODE) {

                    ArrayList<String> selectData = data.getStringArrayListExtra("selectData");

                    if (selectData != null && selectData.size()>0) {
                        for (String path : selectData) {
                            mSelectedPicture.add(path);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_CAMERA://拍照返回
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap=null;
                    Uri uri=data.getData();
                    if (uri!=null) {
                        bitmap= BitmapFactory.decodeFile(uri.getPath());
                    }
                    if (bitmap==null){
                        Bundle bundle=data.getExtras();
                        if (bundle!=null) {
                            bitmap = (Bitmap) bundle.get("data");//缩略图
                        } else {
                            Toast.makeText(context,"拍照失败！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //获取拍照图片路径
                    String picPath= FileUtil.saveBitmapToFile(bitmap, "headImg");

                    mSelectedPicture.add(picPath);

                    if(bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 发布动态
     */
    private void publishDynamic(){

        content = contentEdt.getText().toString();

        dynamic = new Dynamic();

        dynamic.setFromUser(user);

        dynamic.setContent(content);

        dynamic.setTheme(theme);

        dynamic.setImage(pictureList);

        dynamic.setCommentCount(0);
        dynamic.setLikesCount(0);

        //保存到服务器动态表中
        dynamic.save(context, new SaveListener() {
            @Override
            public void onSuccess() {

                Log.i("TAG", "保存到动态表成功");

                //保存动态到个人时间线中
                saveDynamicToMyTimeline();
                //推送动态给粉丝
                pushDynamic();

            }

            @Override
            public void onFailure(int i, String s) {

                handler.sendEmptyMessage(Push_Dynamic_Failure);

                Toast.makeText(PublishDynamicActivity.this, "发布动态失败,请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 保存动态到自己的时间线中
     */
    private void saveDynamicToMyTimeline(){

        if (myTimeline==null) {

            return;
        }

        BmobRelation relation = new BmobRelation();
        relation.add(dynamic);
        myTimeline.setAllDynamic(relation);
        myTimeline.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

                Log.i("TAG", s + i);
            }
        });
    }

    /**
     * 查询自己的时间线
     */
    private void queryMyTimeline(){

        BmobQuery<Timeline> query = new BmobQuery<>();
        query.addWhereEqualTo("fromUser", user);
        query.findObjects(context, new FindListener<Timeline>() {
            @Override
            public void onSuccess(List<Timeline> list) {
                if (list != null && list.size() > 0) {
                    myTimeline = list.get(0);
                }
            }

            @Override
            public void onError(int i, String s) {

                Log.i("TAG", s + i);
            }
        });
    }
    /**
     * 推送动态
     */
    private void pushDynamic(){

        //获取粉丝对象集合
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("toUser", user);

        query.findObjects(context, new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                if (list.size() > 0) { //有粉丝
                    //遍历粉丝集合
                    for (Friend friend : list) {
                        String fansid = friend.getFromUser().getObjectId();
                        //推送给粉丝
                        pushToFans(fansid);
                    }
                } else { // 粉丝为0
                    Log.i("TAG", "粉丝数为0");
                    Message msg = new Message();
                    msg.what = Push_Dynamic_Success;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i("TAG", i + s);
                handler.sendEmptyMessage(Push_Dynamic_Failure);
            }
        });



    }

    /**
     * 推送给粉丝
     * @param fansid
     */
    public void pushToFans(String fansid) {

        //查询粉丝的时间线
        BmobQuery<Timeline> query = new BmobQuery<>();
        User fans = new User();
        fans.setObjectId(fansid);
        query.addWhereEqualTo("fromUser", fans);
        query.findObjects(context, new FindListener<Timeline>() {
            @Override
            public void onSuccess(List<Timeline> list) {
                if (list.size() == 1) {//获取粉丝时间线成功

                    Timeline timeline = list.get(0);
                    BmobRelation relation = new BmobRelation();
                    relation.add(dynamic);
                    timeline.setAllDynamic(relation);
                    timeline.update(context, new UpdateListener() {
                        @Override
                        public void onSuccess() {

                            Message msg= new Message();
                            msg.what = Push_Dynamic_Success;
                            handler.sendMessage(msg);
                            Log.i("TAG","添加给粉丝时间线成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {

                            handler.sendEmptyMessage(Push_Dynamic_Failure);
                            Log.i("TAG",s+i);
                        }
                    });
                } else {

                    handler.sendEmptyMessage(Push_Dynamic_Failure);
                    Log.i("TAG", "推送时间线失败");
                }
            }

            @Override
            public void onError(int i, String s) {

                handler.sendEmptyMessage(Push_Dynamic_Failure);
                Log.i("TAG",i+s);
            }
        });
    }

    /**
     * 显示Dialog
     */
    private void showDialog(){
        AlertDialog dialog = new AlertDialog.Builder(PublishDynamicActivity.this)
                .setMessage("发布动态成功")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PublishDynamicActivity.this.finish();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_publish:

                Log.i("TAG","发布动态");

                if(GeneralUtil.isNetworkAvailable(context)) {

                    if (checkInput()&&isPushFinish) { //有内容

                        isPushFinish = false;
                        showProgressDialog(PublishDynamicActivity.this, "发布中...");

                        if (mSelectedPicture.size() > 0) { //有图片，先上传图片

                            uploadPicture();

                        } else {//无图片
                            //发布动态
                            publishDynamic();
                        }


                    } else { //无内容
                        Toast.makeText(context, "动态不能为空", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "未检测到网络", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_dynamic,menu);
        return true;
    }

    /**
     * 初始化popup
     */
    private void initPopupWindow() {
        View view=getLayoutInflater().inflate(R.layout.popup_get_headimg_layout,null);
        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupRelative = (RelativeLayout) view.findViewById(R.id.popup_relative);
        photoText = (TextView) view.findViewById(R.id.popup_photo);
        cameraText = (TextView) view.findViewById(R.id.popup_camera);
        cancelText = (TextView) view.findViewById(R.id.popup_cancel);

        popupRelative.setOnClickListener(this);
        photoText.setOnClickListener(this);
        cameraText.setOnClickListener(this);
        cancelText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_camera:
                if(GeneralUtil.isSDCard()){
                    Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(cameraIntent.resolveActivity(getPackageManager())!=null){
                        //判断系统是否有能处理cameraIntent的activity
                        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                        popupWindow.dismiss();
                    }
                }else{
                    Toast.makeText(context,"没有检测到SD卡",Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
                break;

            case R.id.popup_photo:
                if(GeneralUtil.isSDCard()){
                    Intent intent=new Intent(this,AlbumActivity.class);
                    startActivityForResult(intent,GET_PHOTO_REQUEST);

                }else{
                    Toast.makeText(context,"没有检测到SD卡",Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
                break;

            case R.id.popup_cancel:
                popupWindow.dismiss();
                break;
            case R.id.popup_relative:
                popupWindow.dismiss();
                break;
        }
    }
}
