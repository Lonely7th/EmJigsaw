package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        tvBarCenter.setText("个人主页");
    }

    @OnClick({R.id.back_btn, R.id.iv_head, R.id.btn_user_name, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.iv_head:
                break;
            case R.id.btn_user_name:
                startActivity(new Intent(PersonalActivity.this,ChangeUserInfoActivity.class));
                break;
            case R.id.btn_logout:
                startActivity(new Intent(PersonalActivity.this,LoginActivity.class));
                break;
        }
    }
}
