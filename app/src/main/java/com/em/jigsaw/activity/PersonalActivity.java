package com.em.jigsaw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.view.SelectDialog;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);

        initUI();
        initData();
        initImagePicker();
    }

    private void initUI() {
        tvBarCenter.setText("个人主页");
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
//        OkGo.<File>post(ServiceAPI.GetUserHead).tag(this)
//                .params("user_no", "")
//                .params("src", file)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//
//                    }
//                });
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
                        ivHead.setImageURI(imageUri);
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(256, 256)// 调整裁剪后的图片最终大小
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
                        ivHead.setImageURI(imageUri);
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(256, 256)// 调整裁剪后的图片最终大小
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
