package com.em.jigsaw.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import com.em.jigsaw.base.ServiceAPI;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.bean.event.RefreshMainFEvent;
import com.em.jigsaw.callback.OnAlterDialogListener;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.SignUtil;
import com.em.jigsaw.utils.SystemUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.utils.UpdateApkUtil;
import com.em.jigsaw.view.dialog.AlertDialog;
import com.em.jigsaw.view.dialog.SelectDialog;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

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

    private Gson gson = new Gson();

    Fragment currentFragment = new Fragment();
    FragmentManager manager;
    int currentTabIndex = 0;

    Fragment mainFragment, plFragment;

    SelectDialog selectDialog = null;
    List<String> selectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();
        initData();
        checkVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
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
     * 刷新用户状态
     */
    private void updateUserInfo(){
        if(LoginUtil.isLogin()){
            OkGo.<String>get(ServiceAPI.GetUserInfo).tag(this)
                    .params("user_no", LoginUtil.getUserInfo().getUserNo())
                    .params("follow_no", LoginUtil.getUserInfo().getUserNo())
                    .params(SignUtil.getParams(true))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            try {
                                JSONObject body = new JSONObject(response.body());
                                if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                    LoginUtil.changeUserInfo(gson.fromJson(body.getString("ResultData"), UserBean.class));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
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
                tab1Tv.setTextColor(getResources().getColor(R.color.colorBlue));
                tab1Iv.setImageDrawable(getResources().getDrawable(R.mipmap.icon_dashboard_s));
                break;
            case 1:
                break;
            case 2:
                tab3Tv.setTextColor(getResources().getColor(R.color.colorBlue));
                tab3Iv.setImageDrawable(getResources().getDrawable(R.mipmap.icon_explore_s));
                break;

        }
    }

    @OnClick({R.id.rl_tab1, R.id.rl_tab2, R.id.rl_tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_tab1:
                if(currentTabIndex == 0){// 刷新
                    EventBus.getDefault().post(new RefreshMainFEvent());
                }else{
                    currentTabIndex = 0;
                    changeBottomStatus();
                    switchFragment(mainFragment);
                }
                break;
            case R.id.rl_tab2:
                if(!LoginUtil.isLogin()){
                    ToastUtil.show(HomeActivity.this,"登录后即可发布新动态");
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    return ;
                }
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
                PictureSelector.create(HomeActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case ContentKey.SelectPic_Gallery:
                PictureSelector.create(HomeActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .isCamera(false)// 是否显示拍照按钮 true or false
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(9,16)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                        .previewImage(false)// 是否可预览图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    /**
     * 获取版本更新信息
     */
    private void checkVersion() {
        OkGo.<String>get(ServiceAPI.GetVerisonCode).tag(this)
                .params(SignUtil.getParams(false))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject body = new JSONObject(response.body());
                            if(body.getInt("ResultCode") == ServiceAPI.HttpSuccess){
                                JSONObject resultData = body.getJSONObject("ResultData");
                                String code = resultData.getString("Code");
                                final String url = resultData.getString("ApkPath");
                                if(!code.equals(SystemUtil.getVersionName(HomeActivity.this))){ // 需要升级
                                    new AlertDialog(HomeActivity.this, resultData.getString("UpdateContent"), "更新", new OnAlterDialogListener() {
                                        @Override
                                        public void onRightClick() {
                                            UpdateApkUtil.downLoadFile(HomeActivity.this,url);
                                        }

                                        @Override
                                        public void onLeftClick() {
                                        }
                                    }).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                        if(localMedia.isCut()){
                            startActivity(new Intent(HomeActivity.this,SelectJStatusActivity.class)
                                    .putExtra("ImageUri",localMedia.getCutPath()));
                        }
                    }
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
