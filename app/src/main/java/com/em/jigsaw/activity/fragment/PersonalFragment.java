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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.activity.FollowListActivity;
import com.em.jigsaw.activity.LoginActivity;
import com.em.jigsaw.activity.MessageActivity;
import com.em.jigsaw.activity.PersonalActivity;
import com.em.jigsaw.activity.ReleaseListActivity;
import com.em.jigsaw.activity.SettingActivity;
import com.em.jigsaw.activity.StarListActivity;
import com.em.jigsaw.base.ContentKey;
import com.em.jigsaw.bean.UserBean;
import com.em.jigsaw.utils.ImgUtil;
import com.em.jigsaw.utils.LoginUtil;
import com.em.jigsaw.utils.ToastUtil;
import com.em.jigsaw.view.RoundImageView;
import com.em.jigsaw.view.dialog.SelectDialog;
import com.em.jigsaw.view.dialog.ShopDialog;
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
    @BindView(R.id.btn_setting)
    RelativeLayout btnSetting;
    @BindView(R.id.iv_head)
    RoundImageView ivHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_vip)
    TextView tvUserVip;
    @BindView(R.id.btn_user_info)
    RelativeLayout btnUserInfo;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn_message)
    LinearLayout btnMessage;
    @BindView(R.id.btn_shop)
    RelativeLayout btnShop;
    @BindView(R.id.iv_release_icon)
    ImageView ivReleaseIcon;
    @BindView(R.id.btn_release)
    RelativeLayout btnRelease;
    @BindView(R.id.iv_star_icon)
    ImageView ivStarIcon;
    @BindView(R.id.btn_star)
    RelativeLayout btnStar;
    @BindView(R.id.iv_follow_icon)
    ImageView ivFollowIcon;
    @BindView(R.id.btn_follow)
    RelativeLayout btnFollow;
    @BindView(R.id.iv_share_icon)
    ImageView ivShareIcon;
    @BindView(R.id.btn_share)
    RelativeLayout btnShare;
    @BindView(R.id.tv_level)
    TextView tvLevel;

    private List<String> shareTypeList = new ArrayList<>();
    private WXShare wxShare;
    private SelectDialog selectDialog;
    private ShopDialog shopDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, rootView);
        initWxShare();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (LoginUtil.isLogin()) {
            UserBean userBean = LoginUtil.getUserInfo();
            tvUserName.setText(userBean.getUserName());
            StringBuilder stringBuilder = new StringBuilder();
            tvUserVip.setText(stringBuilder.append("地址：").append(userBean.getNameCity()).toString());
            if (!TextUtils.isEmpty(userBean.getNameHead())) {
                ImgUtil.loadImg2Account(getActivity(), userBean.getNameHead(), ivHead);
            }
        } else {
            tvUserName.setText("未登录");
            tvUserVip.setText("点击登录");
            ImgUtil.loadImg2Account(getActivity(), "", ivHead);
        }
    }

    private void initWxShare() {
        shareTypeList.add("分享到朋友圈");
        shareTypeList.add("分享给好友");
        //微信分享相关
        wxShare = new WXShare(getActivity());
        wxShare.setListener(new OnResponseListener() {
            @Override
            public void onSuccess() {
                // 分享成功
                ToastUtil.show(getActivity(), "分享成功");
            }

            @Override
            public void onCancel() {
                // 分享取消
                ToastUtil.show(getActivity(), "分享取消");
            }

            @Override
            public void onFail(String message) {
                // 分享失败
                ToastUtil.show(getActivity(), "分享失败");
            }
        });
    }

    @OnClick({R.id.btn_setting, R.id.btn_user_info, R.id.btn_message, R.id.btn_shop, R.id.btn_release, R.id.btn_star, R.id.btn_follow, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.btn_user_info:
                if (LoginUtil.isLogin()) {
                    startActivity(new Intent(getActivity(), PersonalActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.btn_message:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.btn_shop:
                shopDialog = new ShopDialog(getActivity());
                shopDialog.show();
                break;
            case R.id.btn_release:
                if (LoginUtil.isLogin()) {
                    startActivity(new Intent(getActivity(), ReleaseListActivity.class));
                }
                break;
            case R.id.btn_star:
                if (LoginUtil.isLogin()) {
                    startActivity(new Intent(getActivity(), StarListActivity.class));
                }
                break;
            case R.id.btn_follow:
                if (LoginUtil.isLogin()) {
                    startActivity(new Intent(getActivity(), FollowListActivity.class));
                }
                break;
            case R.id.btn_share:
                selectDialog = new SelectDialog(getActivity(), shareTypeList, new SelectDialog.OnSelectListener() {
                    @Override
                    public void onItemSelect(View view, int position, long id) {
                        switch (position) {
                            case 0:
                                wxShare.share(getActivity(), ContentKey.SHARE_URL, ContentKey.SHARE_TITLE, ContentKey.SHARE_CONTENT, 0);
                                break;
                            case 1:
                                wxShare.share(getActivity(), ContentKey.SHARE_URL, ContentKey.SHARE_TITLE, ContentKey.SHARE_CONTENT, 1);
                                break;
                        }
                    }
                });
                selectDialog.show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(selectDialog != null){
            selectDialog.dismiss();
        }
        if(shopDialog != null){
            shopDialog.dismiss();
        }
    }
}
