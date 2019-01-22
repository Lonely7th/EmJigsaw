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
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.dialog.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {

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

    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        tvBarCenter.setText("反馈与建议");
        tvBarRight.setText("提交");
        tvBarRight.setVisibility(View.VISIBLE);

        loadingDialog = new LoadingDialog(FeedbackActivity.this);
    }

    private void toFeedBack(final String content){
        OkGo.<String>post(ServiceAPI.AddFeedBack).tag(this)
                .params("userNo", LoginUtil.isLogin()?LoginUtil.getUserInfo().getUserNo():"")
                .params("content", content)
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            ToastUtil.show(FeedbackActivity.this, "感谢您的反馈");
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        loadingDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        ToastUtil.show(FeedbackActivity.this,"网络异常");
                        loadingDialog.dismiss();
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
                String content = edtContent.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.show(FeedbackActivity.this, "反馈的内容不能为空");
                    return ;
                }
                toFeedBack(content);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }
}
