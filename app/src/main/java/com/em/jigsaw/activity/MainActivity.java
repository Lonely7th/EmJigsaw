package com.em.jigsaw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.em.jigsaw.R;
import com.em.jigsaw.adapter.JigsawAdapter;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.view.JigsawView;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;

    private ImgUtil imgUtil;
    private JigsawAdapter jigsawAdapter;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private ImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        imgUtil = new ImgUtil(MainActivity.this);
        imagePicker = new ImagePicker();
        initUI();
        initData();
        initImagePicker();
    }

    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        // 设置标题
        imagePicker.setTitle("选择图片");
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        jigsawAdapter = new JigsawAdapter(list, MainActivity.this);
        startImagePicker(ContentKey.SelectPic_Gallery,ContentKey.ImgFormat_9_16);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
    }

    /**
     * 刷新jigsawAdapter
     */
    private void updateJigsawList(Bitmap bitmap){
        list.clear();
        list.addAll(imgUtil.getImgArray(bitmap,ContentKey.Format_4_3,ContentKey.ImgFormat_9_16));
        viewJigsaw.setLabels(list,ContentKey.Format_4_3);
    }

    /**
     * 开始选择图片
     */
    private void startImagePicker(int type,final int[] format){
        switch (type){
            case ContentKey.SelectPic_Camera:
                imagePicker.startCamera(MainActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        updateJigsawList(imgUtil.getBitmap(imageUri));
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(format[2], format[3])// 调整裁剪后的图片最终大小
                                .setAspectRatio(format[0], format[1]);// 宽高比
                    }

                    // 用户拒绝授权回调
                    @Override
                    public void onPermissionDenied(int requestCode, String[] permissions, int[] grantResults) {
                    }
                });
                break;
            case ContentKey.SelectPic_Gallery:
                imagePicker.startGallery(MainActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        updateJigsawList(imgUtil.getBitmap(imageUri));
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(format[2], format[3])// 调整裁剪后的图片最终大小
                                .setAspectRatio(format[0], format[1]);// 宽高比
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
