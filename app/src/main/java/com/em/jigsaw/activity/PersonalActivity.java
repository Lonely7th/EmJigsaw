package com.em.jigsaw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.dialog.LoadingDialog;
import com.em.jigsaw.view.dialog.SelectDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

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

    SelectDialog selectDialog = null;
    List<String> selectList = new ArrayList<>();

    UserBean userBean;
    LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);

        initUI();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initUI() {
        tvBarCenter.setText("个人资料");
        loadingDialog = new LoadingDialog(PersonalActivity.this);
    }

    private void updateUI(){
        userBean = LoginUtil.getUserInfo();
        tvUserName.setText(userBean.getUserName());
        tvUserId.setText(userBean.getUserNo());
        tvUserPhone.setText(userBean.getUserPhone());
        if(!TextUtils.isEmpty(userBean.getNameHead())){
            Glide.with(PersonalActivity.this).load(userBean.getNameHead()).into(ivHead);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        selectList.add("相册选择");
        selectList.add("拍照选择");
    }

    /**
     * 修改用户头像
     */
    private void changeUserHead(File file){
        OkGo.<String>post(ServiceAPI.GetUserHead).tag(this)
                .params("user_no", LoginUtil.getUserInfo().getUserNo())
                .params("res", file)
                .params(SignUtil.getParams(true))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                ToastUtil.show(PersonalActivity.this,"操作成功");
                                UserBean userBean = LoginUtil.getUserInfo();
                                userBean.setNameHead(body.getString("ResultData"));
                                LoginUtil.changeUserInfo(userBean);
                                updateUI();
                            }else{
                                ToastUtil.show(PersonalActivity.this,"网络异常");
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
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadingDialog.dismiss();
                    }
                });
    }

    @OnClick({R.id.back_btn, R.id.iv_head, R.id.btn_user_name, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.iv_head:
                selectDialog = new SelectDialog(PersonalActivity.this, selectList, (view1, position, id) -> {
                    switch (position){
                        case 0:
                            startImagePicker(ContentKey.SelectPic_Gallery);
                            break;
                        case 1:
                            startImagePicker(ContentKey.SelectPic_Camera);
                            break;
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
                finish();
                break;
        }
    }

    private void startImagePicker(int type) {
        switch (type) {
            case ContentKey.SelectPic_Camera:
                PictureSelector.create(PersonalActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case ContentKey.SelectPic_Gallery:
                PictureSelector.create(PersonalActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .isCamera(false)// 是否显示拍照按钮 true or false
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                        .previewImage(false)// 是否可预览图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList.size() > 0){
                        LocalMedia localMedia = selectList.get(0);
                        if(localMedia.isCut() && localMedia.isCompressed()){
                            changeUserHead(new File(localMedia.getCompressPath()));
                        }
                    }
                    break;
            }
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
