package com.em.jigsaw.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.em.jigsaw.activity.fragment.MainFragment;
import com.em.jigsaw.activity.fragment.PersonalFragment;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.view.SelectDialog;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.tab1_Iv)
    ImageView tab1Iv;
    @BindView(R.id.tab1_tv)
    TextView tab1Tv;
    @BindView(R.id.rl_tab1)
    RelativeLayout rlTab1;
    @BindView(R.id.tab2_Iv)
    ImageView tab2Iv;
    @BindView(R.id.rl_tab2)
    RelativeLayout rlTab2;
    @BindView(R.id.tab3_Iv)
    ImageView tab3Iv;
    @BindView(R.id.tab3_tv)
    TextView tab3Tv;
    @BindView(R.id.rl_tab3)
    RelativeLayout rlTab3;

    Fragment currentFragment = new Fragment();
    FragmentManager manager;
    int currentTabIndex = 0;

    Fragment mainFragment, plFragment;

    SelectDialog selectDialog = null;
    List<String> selectList = new ArrayList<>();

    ImagePicker imagePicker;
    int[] ImgFormat = ContentKey.ImgFormat_9_16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();
        initData();
        initImagePicker();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        selectList.add("相册选择");
        selectList.add("拍照选择");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        manager = getFragmentManager();
        mainFragment = new MainFragment();
        plFragment = new PersonalFragment();
        switchFragment(mainFragment);
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
     * 切换Fragment
     */
    private void switchFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.home_fragment, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }

    /**
     * 清空底部所有图标的状态
     */
    private void clearBottomIcon() {
        tab1Tv.setTextColor(getResources().getColor(R.color.colorGary));
        tab1Iv.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dashboard_n));
        tab3Tv.setTextColor(getResources().getColor(R.color.colorGary));
        tab3Iv.setImageDrawable(getResources().getDrawable(R.mipmap.icon_explore_n));
    }

    /**
     * 切换底部的状态
     */
    private void changeBottomStatus() {
        clearBottomIcon(); //先清空
        switch (currentTabIndex){
            case 0:
                tab1Tv.setTextColor(getResources().getColor(R.color.colorBlack));
                tab1Iv.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dashboard_s));
                break;
            case 1:
                break;
            case 2:
                tab3Tv.setTextColor(getResources().getColor(R.color.colorBlack));
                tab3Iv.setImageDrawable(getResources().getDrawable(R.mipmap.icon_explore_s));
                break;

        }
    }

    @OnClick({R.id.rl_tab1, R.id.rl_tab2, R.id.rl_tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_tab1:
                currentTabIndex = 0;
                changeBottomStatus();
                switchFragment(mainFragment);
                break;
            case R.id.rl_tab2:
                selectDialog = new SelectDialog(HomeActivity.this, selectList, new SelectDialog.OnSelectListener() {
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
            case R.id.rl_tab3:
                currentTabIndex = 2;
                changeBottomStatus();
                switchFragment(plFragment);
                break;
        }
    }

    /**
     * 开始选择图片
     */
    private void startImagePicker(int type) {
        switch (type) {
            case ContentKey.SelectPic_Camera:
                imagePicker.startCamera(HomeActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        startActivity(new Intent(HomeActivity.this,
                                AddJigsawActivity.class).putExtra("imageUri",imageUri.toString()));
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
                imagePicker.startGallery(HomeActivity.this, new ImagePicker.Callback() {
                    // 选择图片回调
                    @Override
                    public void onPickImage(Uri imageUri) {

                    }

                    // 裁剪图片回调
                    @Override
                    public void onCropImage(Uri imageUri) {
                        startActivity(new Intent(HomeActivity.this,
                                AddJigsawActivity.class).putExtra("imageUri",imageUri.toString()));
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
