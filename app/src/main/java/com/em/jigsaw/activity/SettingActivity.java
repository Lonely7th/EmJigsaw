package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SystemUtil;
import com.em.jigsaw.utils.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

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
    @BindView(R.id.btn_clear)
    RelativeLayout btnClear;
    @BindView(R.id.btn_feedback)
    RelativeLayout btnFeedback;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_logout)
    RelativeLayout btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        
        initUI();
    }

    private void initUI() {
        tvBarCenter.setText("设置");
        tvVersion.setText("V " + SystemUtil.getVersionName(SettingActivity.this));
    }

    /**
     * 清空缓存
     */
    private void clearMeDir(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //遍历crash文件夹
                String path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/pic0/";
                File dir = new File(path);
                File[] files = dir.listFiles();
                for (File item : files) {
                    if (!item.isDirectory()) {
                        item.delete();
                    }
                }
            }
        }).start();
    }

    @OnClick({R.id.back_btn, R.id.btn_clear, R.id.btn_feedback, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_clear:
                clearMeDir();
                ToastUtil.show(SettingActivity.this,"清除缓存成功");
                break;
            case R.id.btn_feedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;
            case R.id.btn_logout:
                LoginUtil.exitLogin();
                finish();
                break;
        }
    }
}
