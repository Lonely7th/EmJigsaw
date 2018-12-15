package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.activity.PersonalActivity;
import com.em.jigsaw.activity.ReleaseListActivity;
import com.em.jigsaw.activity.StarListActivity;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, rootView);
        initUI();
        return rootView;
    }

    private void initUI() {
        backBtn.setVisibility(View.GONE);
        tvBarCenter.setText("个人主页");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btn_user_info, R.id.btn_release, R.id.btn_star, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_user_info:
                startActivity(new Intent(getActivity(),PersonalActivity.class));
                break;
            case R.id.btn_release:
                startActivity(new Intent(getActivity(),ReleaseListActivity.class));
                break;
            case R.id.btn_star:
                startActivity(new Intent(getActivity(),StarListActivity.class));
                break;
            case R.id.btn_share:
                //TODO 分享给好友
                break;
        }
    }
}
