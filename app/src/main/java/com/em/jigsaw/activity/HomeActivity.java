package com.em.jigsaw.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.activity.fragment.MainFragment;
import com.em.jigsaw.activity.fragment.PersonalFragment;

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
    @BindView(R.id.tab2_tv)
    TextView tab2Tv;
    @BindView(R.id.rl_tab2)
    RelativeLayout rlTab2;
    @BindView(R.id.tab3_Iv)
    ImageView tab3Iv;
    @BindView(R.id.tab3_tv)
    TextView tab3Tv;
    @BindView(R.id.rl_tab3)
    RelativeLayout rlTab3;

    Fragment currentFragment = new Fragment();
    FragmentManager manager;
    int currentTabIndex = 0;

    Fragment mainFragment, plFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();
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
        tab1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
        tab2Tv.setTextColor(getResources().getColor(R.color.colorGary));
        tab2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
        tab3Tv.setTextColor(getResources().getColor(R.color.colorGary));
        tab3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
    }

    /**
     * 切换底部的状态
     */
    private void changeBottomStatus() {
        clearBottomIcon(); //先清空
        switch (currentTabIndex){
            case 0:
                tab1Tv.setTextColor(getResources().getColor(R.color.colorBlack));
                tab1Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                break;
            case 1:
                tab2Tv.setTextColor(getResources().getColor(R.color.colorBlack));
                tab2Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                break;
            case 2:
                tab3Tv.setTextColor(getResources().getColor(R.color.colorBlack));
                tab3Iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_background));
                break;

        }
    }

    @OnClick({R.id.rl_tab1, R.id.rl_tab2, R.id.rl_tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_tab1:
                currentTabIndex = 0;
                changeBottomStatus();
                switchFragment(mainFragment);
                break;
            case R.id.rl_tab2:
                //TODO 发布
                break;
            case R.id.rl_tab3:
                currentTabIndex = 2;
                changeBottomStatus();
                switchFragment(plFragment);
                break;
        }
    }
}
