package com.em.jigsaw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeUserInfoActivity extends AppCompatActivity {

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
    @BindView(R.id.edt_content)
    EditText edtContent;

    private String cType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        cType = getIntent().getStringExtra("cType");
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {
        tvBarCenter.setText("修改用户信息");
        tvBarRight.setText("保存");
        tvBarRight.setVisibility(View.VISIBLE);
    }

    private void changeUserInfo(){
        final String content = edtContent.getText().toString();

        OkGo.<String>post(ServiceAPI.ChangeInfo).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params("content", content)
                .params("ctype", cType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                ToastUtil.show(ChangeUserInfoActivity.this,"操作成功");
                                UserBean userBean = LoginUtil.getUserInfo();
                                userBean.setUserName(content);
                                LoginUtil.changeUserInfo(userBean);
                                finish();
                            }else{
                                ToastUtil.show(ChangeUserInfoActivity.this,"网络异常");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.right_btn:
                if(TextUtils.isEmpty(edtContent.getText().toString())){
                    ToastUtil.show(ChangeUserInfoActivity.this,"内容不能为空");
                }else{
                    changeUserInfo();
                }
                break;
        }
    }
}
