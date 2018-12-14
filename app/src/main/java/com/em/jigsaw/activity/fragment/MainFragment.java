package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Time ： 2018/12/11 0011 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class MainFragment extends Fragment {
    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        initUI();
        return rootView;
    }

    private void initUI() {
        backBtn.setVisibility(View.GONE);
        tvBarCenter.setText("发现 8 条新内容");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
