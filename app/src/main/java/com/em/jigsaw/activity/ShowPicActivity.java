package com.em.jigsaw.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.PermissionUtil;
import com.em.jigsaw.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.em.jigsaw.utils.PermissionUtil.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class ShowPicActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.right_btn)
    RelativeLayout rightBtn;
    @BindView(R.id.iv_content)
    ImageView ivContent;

    private String headPath;
    private ImgUtil imgUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        ButterKnife.bind(this);
        headPath = getIntent().getStringExtra("uri");
        imgUtil = new ImgUtil(ShowPicActivity.this);
        
        initUI();
    }

    private void initUI() {
        tvBarRight.setText("保存");
        tvBarRight.setVisibility(View.VISIBLE);
        Glide.with(ShowPicActivity.this).load(headPath).into(ivContent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imgUtil.saveImageToGallery(headPath);
                    ToastUtil.show(ShowPicActivity.this,"保存到相册成功");
                } else {
                    ToastUtil.show(ShowPicActivity.this,"暂无权限");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick({R.id.back_btn, R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                if (PermissionUtil.checkPermissionREAD_EXTERNAL_STORAGE(ShowPicActivity.this)) {
                    imgUtil.saveImageToGallery(headPath);
                    ToastUtil.show(ShowPicActivity.this,"保存到相册成功");
                }
                break;
        }
    }
}
