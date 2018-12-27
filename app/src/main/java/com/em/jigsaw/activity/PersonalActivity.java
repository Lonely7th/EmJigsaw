package com.em.jigsaw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.SelectDialog;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.btn_user_name)
    RelativeLayout btnUserName;
    @BindView(R.id.btn_logout)
    RelativeLayout btnLogout;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;

    ImagePicker imagePicker;
    SelectDialog selectDialog = null;
    List<String> selectList = new ArrayList<>();

    UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);

        initUI();
        initData();
        initImagePicker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initUI() {
        tvBarCenter.setText("个人主页");
    }

    private void updateUI(){
        userBean = LoginUtil.getUserInfo();
        tvUserName.setText(userBean.getUserName());
        tvUserId.setText(userBean.getUserNo());
        tvUserPhone.setText(userBean.getUserPhone());
        if(!TextUtils.isEmpty(userBean.getNameHead())){
            Glide.with(PersonalActivity.this).load(userBean.getNameHead()).into(ivHead);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        selectList.add("相册选择");
        selectList.add("拍照选择");
    }

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        imagePicker = new ImagePicker();
        // 设置标题
        imagePicker.setTitle("选择图片");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
    }

    /**
     * 修改用户头像
     */
    private void changeUserHead(File file){
        OkGo.<String>post(ServiceAPI.GetUserHead).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params("src", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                ToastUtil.show(PersonalActivity.this,"操作成功");
                                UserBean userBean = LoginUtil.getUserInfo();
                                userBean.setNameHead(body.getString("ResultData"));
                                LoginUtil.changeUserInfo(userBean);
                            }else{
                                ToastUtil.show(PersonalActivity.this,"网络异常");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.iv_head, R.id.btn_user_name, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.iv_head:
                selectDialog = new SelectDialog(PersonalActivity.this, selectList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        switch (position){
                            case 0:
                                startImagePicker(ContentKey.SelectPic_Gallery);
                                break;
                            case 1:
                                startImagePicker(ContentKey.SelectPic_Camera);
                                break;
                        }
                    }
                });
                selectDialog.show();
                break;
            case R.id.btn_user_name:
                startActivity(new Intent(PersonalActivity.this, ChangeUserInfoActivity.class)
                        .putExtra("cType","0"));
                break;
            case R.id.btn_logout:
                LoginUtil.exitLogin();
                finish();
                break;
        }
    }

    private void startImagePicker(int type) {
        switch (type) {
            case ContentKey.SelectPic_Camera:
                imagePicker.startCamera(PersonalActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        try {
                            changeUserHead(new File(new URI(imageUri.toString())));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(128, 128)// 调整裁剪后的图片最终大小
                                .setAspectRatio(1, 1);// 宽高比
                    }

                    // 用户拒绝授权回调
                    @Override
                    public void onPermissionDenied(int requestCode, String[] permissions, int[] grantResults) {
                    }
                });
                break;
            case ContentKey.SelectPic_Gallery:
                imagePicker.startGallery(PersonalActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        try {
                            changeUserHead(new File(new URI(imageUri.toString())));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(128, 128)// 调整裁剪后的图片最终大小
                                .setAspectRatio(1, 1);// 宽高比
                    }

                    // 用户拒绝授权回调
                    @Override
                    public void onPermissionDenied(int requestCode, String[] permissions, int[] grantResults) {
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
