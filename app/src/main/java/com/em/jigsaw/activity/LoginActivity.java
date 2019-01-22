package com.em.jigsaw.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.PhoneFormatCheckUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.btn_login)
    Button btnLogin;

    //是否可以获取验证码
    private boolean getVerCode = false;
    //是否可以登录
    private boolean toLogin = false;

    private int countDown = 0;
    private Timer timer = new Timer();

    private LoadingDialog loadingDialog = null;

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    toLogin();
                }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    ToastUtil.show(LoginActivity.this, "获取验证码成功");
                }
            } else if (result == SMSSDK.RESULT_ERROR) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(LoginActivity.this, "验证码错误");
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SMSSDK.registerEventHandler(eh);
        initUI();
    }

    private void initUI() {
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(PhoneFormatCheckUtil.isPhoneLegal(s.toString())){
                    btnGetCode.setAlpha(1.0f);
                    getVerCode = true;
                }else{
                    btnGetCode.setAlpha(0.6f);
                    getVerCode = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    btnLogin.setAlpha(0.6f);
                    toLogin = false;
                }else{
                    btnLogin.setAlpha(1.0f);
                    toLogin = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //开启定时器
        timer.schedule(timerTask, 1000, 1000);

        loadingDialog = new LoadingDialog(LoginActivity.this);
    }

    /**
     * 获取验证码
     */
    private void getVerCode() {
        if (PhoneFormatCheckUtil.isPhoneLegal(edtPhone.getText().toString().trim())) {
            //开始倒计时60s
            startTimer();
            //开始网络请求
            SMSSDK.getVerificationCode("86", edtPhone.getText().toString().trim());
        } else {
            ToastUtil.show(this, "请输入正确的手机号");
        }
    }

    /**
     * 开始登录
     */
    private void toLogin(){
        OkGo.<String>post(ServiceAPI.Login).tag(this)
                .params("phoneNumber", edtPhone.getText().toString().trim())
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                LoginUtil.loginSuccess(body.getString("ResultData"));
                                ToastUtil.show(LoginActivity.this,"登录成功");
                                finish();
                            }else{
                                ToastUtil.show(LoginActivity.this,"网络异常");
                            }
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
                        ToastUtil.show(LoginActivity.this,"网络异常");
                        loadingDialog.dismiss();
                    }
                });
    }

    /**
     * 开始倒计时
     */
    private void startTimer() {
        getVerCode = false;
        countDown = 60;
    }

    /**
     * 倒计时相关
     */
    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    btnGetCode.setText(countDown + "s");
                    if (countDown <= 0) {
                        getVerCode = true;
                        btnGetCode.setText("获取动态密码");
                    }
            }
            super.handleMessage(msg);
        }
    };
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (countDown > 0) {
                countDown--;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if(loadingDialog != null){
            loadingDialog.dismiss();
        }
    }

    @OnClick({R.id.back_btn, R.id.btn_get_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_get_code:
                if(getVerCode){
                    getVerCode();
                }
                break;
            case R.id.btn_login:
                if(toLogin){
                    SMSSDK.submitVerificationCode("86", edtPhone.getText().toString(), edtPassword.getText().toString());
                }
                break;
        }
    }

    protected void onStop() {
        super.onStop();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterEventHandler(eh);
    }
}
