package com.em.jigsaw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.JigsawImgBean;
import com.em.jigsaw.callback.OnJigsawChangedListener;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.JigsawView;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //private static final String TAG = "MainActivity";

    @BindView(R.id.view_jigsaw)
    JigsawView viewJigsaw;
    @BindView(R.id.iv_content)
    ImageView ivContent;

    private ImgUtil imgUtil;
    private ArrayList<JigsawImgBean> list = new ArrayList<>();

    private ImagePicker imagePicker;

    private int[] ImgFormat = ContentKey.ImgFormat_9_16;
    private int[] CropFormat = ContentKey.Format_6_4;

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
        startImagePicker(ContentKey.SelectPic_Gallery);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        viewJigsaw.setOnChangedListener(new OnJigsawChangedListener() {
            @Override
            public void onChanged(ArrayList<JigsawImgBean> arrayList) {
                if(imgUtil.jigsawSuccess(arrayList)){
                    ToastUtil.show(MainActivity.this,"成功");
                    ivContent.setVisibility(View.VISIBLE);
                    viewJigsaw.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 刷新jigsawAdapter
     */
    private void updateJigsawList(Bitmap bitmap) {
        list.clear();
        list.addAll(imgUtil.sortImgArray(imgUtil.getImgArray(bitmap, CropFormat, ImgFormat)));
        viewJigsaw.setLabels(list);
    }

    /**
     * 开始选择图片
     */
    private void startImagePicker(int type) {
        switch (type) {
            case ContentKey.SelectPic_Camera:
                imagePicker.startCamera(MainActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        ivContent.setImageURI(imageUri);
                        updateJigsawList(imgUtil.getBitmap(imageUri));
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(ImgFormat[2], ImgFormat[3])// 调整裁剪后的图片最终大小
                                .setAspectRatio(ImgFormat[0], ImgFormat[1]);// 宽高比
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
                        ivContent.setImageURI(imageUri);
                        updateJigsawList(imgUtil.getBitmap(imageUri));
                    }

                    // 自定义裁剪配置
                    @Override
                    public void cropConfig(CropImage.ActivityBuilder builder) {
                        builder.setMultiTouchEnabled(false)// 是否启动多点触摸
                                .setGuidelines(CropImageView.Guidelines.OFF)// 设置网格显示模式
                                .setCropShape(CropImageView.CropShape.RECTANGLE)// 圆形/矩形
                                .setRequestedSize(ImgFormat[2], ImgFormat[3])// 调整裁剪后的图片最终大小
                                .setAspectRatio(ImgFormat[0], ImgFormat[1]);// 宽高比
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
