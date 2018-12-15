package com.em.jigsaw.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.em.jigsaw.bean.JigsawListBean;

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
    @BindView(R.id.back_btn)
    RelativeLayout backBtn;
    @BindView(R.id.tv_bar_center)
    TextView tvBarCenter;
    @BindView(R.id.main_listview)
    ListView mainListview;

    private List<JigsawListBean> list = new ArrayList<>();
    private JigsawListAdapter jigsawListAdapter;

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
        jigsawListAdapter = new JigsawListAdapter(list,getActivity());
        mainListview.setAdapter(jigsawListAdapter);
        mainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(),JigsawViewActivity.class));
            }
        });
    }

    private void initUI() {
        backBtn.setVisibility(View.GONE);
        tvBarCenter.setText("发现 8 条新内容");
    }

    private void loadData(){
        for(int i = 0;i < 10;i++){
            JigsawListBean bean = new JigsawListBean();
            list.add(bean);
        }
        jigsawListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
