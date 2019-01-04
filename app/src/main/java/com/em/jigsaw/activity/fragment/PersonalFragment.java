package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.em.jigsaw.R;
import com.em.jigsaw.activity.LoginActivity;
import com.em.jigsaw.activity.PersonalActivity;
import com.em.jigsaw.activity.ReleaseListActivity;
import com.em.jigsaw.activity.StarListActivity;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.dialog.SelectDialog;
import com.em.jigsaw.wxapi.OnResponseListener;
import com.em.jigsaw.wxapi.WXShare;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Time ： 2018/12/11 0011 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class PersonalFragment extends Fragment {
    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.btn_user_info)
    RelativeLayout btnUserInfo;
    @BindView(R.id.iv_release_icon)
    ImageView ivReleaseIcon;
    @BindView(R.id.btn_release)
    RelativeLayout btnRelease;
    @BindView(R.id.iv_star_icon)
    ImageView ivStarIcon;
    @BindView(R.id.btn_star)
    RelativeLayout btnStar;
    @BindView(R.id.iv_share_icon)
    ImageView ivShareIcon;
    @BindView(R.id.btn_share)
    RelativeLayout btnShare;

    WXShare wxShare;
    SelectDialog selectDialog;
    List<String> shareTypeList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, rootView);
        initUI();
        initData();
        initWxShare();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo();
    }

    private void initUI() {
        backBtn.setVisibility(View.GONE);
        tvBarCenter.setText("个人主页");
    }

    private void initData() {
        shareTypeList.add("分享到朋友圈");
        shareTypeList.add("分享给好友");
    }

    private void updateUserInfo(){
        if(LoginUtil.isLogin()){
            UserBean userBean = LoginUtil.getUserInfo();
            tvUserName.setText(userBean.getUserName());
            StringBuilder stringBuilder = new StringBuilder();
            tvUserId.setText(stringBuilder.append("user_id：").append(userBean.getUserNo()).toString());
            if(!TextUtils.isEmpty(userBean.getNameHead())){
                Glide.with(getActivity()).load(userBean.getNameHead()).into(ivHead);
            }
        }else{
            tvUserName.setText("未登录");
            tvUserId.setText("点击登录");
            Glide.with(getActivity()).load("").into(ivHead);
        }
    }

    private void initWxShare() {
        //微信分享相关
        wxShare = new WXShare(getActivity());
        wxShare.setListener(new OnResponseListener() {
            @Override
            public void onSuccess() {
                // 分享成功
                ToastUtil.show(getActivity(),"分享成功");
            }

            @Override
            public void onCancel() {
                // 分享取消
                ToastUtil.show(getActivity(),"分享取消");
            }

            @Override
            public void onFail(String message) {
                // 分享失败
                ToastUtil.show(getActivity(),"分享失败");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_user_info, R.id.btn_release, R.id.btn_star, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_user_info:
                if(LoginUtil.isLogin()){
                    startActivity(new Intent(getActivity(),PersonalActivity.class));
                }else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
                break;
            case R.id.btn_share:
                selectDialog = new SelectDialog(getActivity(), shareTypeList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        switch (position){
                            case 0:
                                wxShare.share(getActivity(),ContentKey.SHARE_URL,ContentKey.SHARE_TITLE,ContentKey.SHARE_CONTENT,0);
                                break;
                            case 1:
                                wxShare.share(getActivity(),ContentKey.SHARE_URL,ContentKey.SHARE_TITLE,ContentKey.SHARE_CONTENT,1);
                                break;
                        }
                    }
                });
                selectDialog.show();
                break;
            case R.id.btn_release:
                if(LoginUtil.isLogin()){
                    startActivity(new Intent(getActivity(),ReleaseListActivity.class));
                }
                break;
            case R.id.btn_star:
                if(LoginUtil.isLogin()){
                    startActivity(new Intent(getActivity(),StarListActivity.class));
                }
                break;
        }
    }
}
