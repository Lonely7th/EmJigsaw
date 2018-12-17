package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.em.jigsaw.R;
import com.em.jigsaw.activity.JigsawViewActivity;
import com.em.jigsaw.adapter.JigsawListAdapter;
import com.em.jigsaw.adapter.TopBarAdapter;
import com.em.jigsaw.bean.JigsawListBean;
import com.em.jigsaw.bean.MainTopBarBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Time ： 2018/12/11 0011 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class MainFragment extends Fragment {
    private final static String TAG = "MainFragment";
    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.main_listview)
    ListView mainListview;
    @BindView(R.id.topbar_view)
    android.support.v7.widget.RecyclerView topbarView;

    private List<JigsawListBean> list = new ArrayList<>();
    private JigsawListAdapter jigsawListAdapter;
    private ArrayList<MainTopBarBean> topBarBeanList = new ArrayList<>();
    private TopBarAdapter topBarAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        initUI();
        initData();
        loadData();
        return rootView;
    }

    private void initData() {
        jigsawListAdapter = new JigsawListAdapter(list, getActivity());
        mainListview.setAdapter(jigsawListAdapter);
        mainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), JigsawViewActivity.class));
            }
        });

    }

    private void initUI() {
        backBtn.setVisibility(View.GONE);
        tvBarCenter.setText("发现 8 条新内容");

        topbarView.setHasFixedSize(true);//设置固定大小
        topbarView.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage=new LinearLayoutManager(getActivity());
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        topbarView.setLayoutManager(mLayoutManage);
        topBarAdapter = new TopBarAdapter(getActivity(),topBarBeanList);
        topbarView.setAdapter(topBarAdapter);

        topBarAdapter.setOnItemClickListener(new TopBarAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                for(int i = 0;i < topBarBeanList.size();i++){
                    if(i == position){
                        topBarBeanList.get(i).setSelect(true);
                    }else{
                        topBarBeanList.get(i).setSelect(false);
                    }
                }
                topBarAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            JigsawListBean bean = new JigsawListBean();
            list.add(bean);
            MainTopBarBean barBean = new MainTopBarBean();
            barBean.setTitle("item-"+i);
            if(i == 0){
                barBean.setSelect(true);
            }
            topBarBeanList.add(barBean);
        }
        jigsawListAdapter.notifyDataSetChanged();
        topBarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
